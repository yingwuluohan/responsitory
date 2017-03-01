package com.common.utils.dbManage;

import java.util.ArrayList;

/**
 * 数据集群节点监控，单独的一个线程，定时监控，可以根据ClusterNode的类型不同
 * 自己定义不同的监控方式。例如读库可以定义自动恢复，读写库和读多写库定义发送监控邮件或者短信
 * @author pgf
 *
 */
public class ClusterNodeMonitor implements Runnable {
	private long interval = 60 * 1000;
	private Thread thread;
	private boolean alive = true;
	//private final DbCluster cluster;
	private ArrayList<ClusterNode> clusterNodes = new ArrayList<ClusterNode>(5);
	private ArrayList<ClusterNode> badNodes = new ArrayList<ClusterNode>();

	public ClusterNodeMonitor(){//DbCluster cluster) {
		//this.cluster = cluster;
	}
	/**
	 * 设置监控间隔
	 * @param interval
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}

	public void start(boolean daemon) {
		thread = new Thread(this);
		thread.setDaemon(daemon);
		thread.start();
	}
	/**
	 * 添加监控节点
	 * @param clusterNode 监控节点
	 */
	public void addClusterNode(ClusterNode clusterNode) {
		if(clusterNode==null) return;
		this.clusterNodes.add(clusterNode);
	}
	/**
	 * 获取所有活动的监控节点
	 * @return 返回所有活动的节点集合
	 */
	public ArrayList<ClusterNode> getActiveNodes() {
		return this.clusterNodes;
	}

	public void run() {
		if (interval <= 0)
			throw new IllegalArgumentException("interval must be >0");
		while (alive) {
			sleep();
			forceDetectingBadNode(); // 先检查错误的节点是否恢复
			forceDetectingNode(); // 再检查现有的节点是否正常
		}
	}
	
	/**
	 * 让线程手工停止
	 */
	public void setStop() {
		alive = false;
		thread = null;
	}
	
	private void sleep() {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理坏的节点
	 */
	private void forceDetectingBadNode() {
		if (badNodes.size() > 0) { // 如果有以前的坏节点
			ArrayList<ClusterNode> resumeNodes = new ArrayList<ClusterNode>(badNodes.size());
			for (int i = 0; i < badNodes.size(); i++) {
				ClusterNode node = badNodes.get(i);
				boolean result = node.checkStatus();
				try {
					if (result) { // 如果发现状态正常，那么假如到恢复节点中
						resumeNodes.add(node);
					}
				} catch (Exception ex) {
				}
			}
			if (resumeNodes.size() > 0) { // 如果有恢复的节点，那么自动加入进去
				for (ClusterNode rNode : resumeNodes) {
					badNodes.remove(rNode);
				}
				synchronized (clusterNodes) {
					clusterNodes.addAll(resumeNodes);
				}
				handleResumeNodeChanged(resumeNodes);
			}

		}
	}
	
	/**
	 * 检查节点的状态
	 */
	private void forceDetectingNode() {
		int threadCount = clusterNodes.size();
		if(threadCount==0) return;
		ArrayList<ClusterNode> activeNodes = new ArrayList<ClusterNode>();
		ArrayList<ClusterNode> faultNodes = new ArrayList<ClusterNode>();
		for (int i = 0; i < threadCount; i++) {
			ClusterNode node = clusterNodes.get(i);
			boolean result = node.checkStatus();
			try {
//			System.out.println("node....."+node.getNodeId()+",checkstatus="+result);
				if (result) {
					activeNodes.add(node);
				} else {
					faultNodes.add(node);
				}
			} catch (Exception ex) {
				faultNodes.add(node);
			}
		}
		if (faultNodes.size() > 0) { // 如果检查到错误的节点有变更，执行handleNodeChanged()方法
			synchronized (clusterNodes) {
				clusterNodes.clear();
				clusterNodes.addAll(activeNodes);
				badNodes.addAll(faultNodes);
				System.out.println("Cluster Node List" + clusterNodes);
				System.out.println("bad Node List" + badNodes);
				handleFaultNodeChanged(faultNodes);
			}
		}
	}
	
	/**
	 * 处理所有坏的节点
	 * @param faultNodes 所有坏的节点
	 */
	private void handleFaultNodeChanged(ArrayList<ClusterNode> faultNodes) {
		for (ClusterNode node : faultNodes) {
			node.handleFault();
		}
	}
	
	/**
	 * 处理节点如何恢复
	 * @param resumeNodes 需要恢复的节点
	 */
	private void handleResumeNodeChanged(ArrayList<ClusterNode> resumeNodes) {
		for (ClusterNode node : resumeNodes) {
			node.resumeFault();
		}
	}

}
