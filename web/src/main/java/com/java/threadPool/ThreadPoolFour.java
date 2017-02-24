package com.java.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** 
* @author  dfn  : 
* @date 创建时间：2016年3月8日 下午5:40:22 
* @version 1.0 
* @parameter   
* @return  
*/
public class ThreadPoolFour {
	/**
	 * Java通过Executors提供四种线程池，分别为：
newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，
保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
	 */
	
	public static void main( String[] args ){
		ThreadPoolFour thread = new ThreadPoolFour();
//		thread.getCatchePool();
		thread.getScheduledPool();
		
	}
	public void getCatchePool(){
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			final int index = i;
			try {
				Thread.sleep(index * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 
			cachedThreadPool.execute(new Runnable() {
		 
				@Override
				public void run() {
					Thread current = Thread.currentThread();  
					System.out.println( current.getId()+ ":" + index);
				}
			});
		}
	}
	public void getFixedPool(){
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 10; i++) {
			final int index = i;
			fixedThreadPool.execute(new Runnable() {
		 
				@Override
				public void run() {
					try {
						System.out.println(index);
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}
	public void getScheduledPool(){
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
		//延迟执行示例代码如下：
		scheduledThreadPool.schedule(new Runnable() {
		 
			@Override
			public void run() {
				System.out.println("delay 3 seconds");
			}
		}, 3, TimeUnit.SECONDS);
		//定期执行示例代码如下：
		scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
			 
			@Override
			public void run() {
				System.out.println("delay 1 seconds, and excute every 3 seconds");
			}
		}, 1, 3, TimeUnit.SECONDS);
	}
	
	
	
	
	
	
	
	
	
	
}
