package com.common.utils.dbManage;

public interface ClusterNode {
	public static final AlarmNotice alarmNotice = DefaultAlarmNotice.getInstance();
    
	/**
     * 获取当前节点名称
     * @return String
     */
    public String getNodeId();
    
    /**
     * 查看节点的状态是否正常
     * @return boolean true表示节点正常，false表示节点失败
     */
    public boolean checkStatus();
    
    /**
     * 如果检查到错误节点，对此节点的处理是如何的
     */
    public void handleFault();

    /**
     * 如果节点已经恢复，那么从错误恢复
     */
    public void resumeFault();
}
