package com.java.threadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** 
* @author  dfn  : 
* @date 创建时间：2016年3月7日 下午6:34:36 
* @version 1.0 
* @parameter   
* @return  
*/
public class ThreadPoolTest {
	 private static final ThreadPoolExecutor shutdownExecutor = new ThreadPoolExecutor(2, 6,
	            1000L, TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<Runnable>(100) ); 
	 
	 public void execut(){
		 shutdownExecutor.execute( new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for( int i = 0 ;i < 4 ;i++ ){
				System.out.println( "线程池" );
				}
			}
			 
		 });
	 }
	 public void post(){
		 shutdownExecutor.execute( new timeRunnble() );
	 }
	 private class timeRunnble implements Runnable{

		 
		@Override
		public void run() {
			System.out.println( "内部类重写" );
			
		}
		 
	 }
	 public static void main( String[] args ){
		 ThreadPoolTest test = new ThreadPoolTest();
		 test.post();
	 }
}
