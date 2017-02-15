package com.java.fanxing;
/** 
* @author  dfn  : 
* @date 创建时间：2015年12月15日 下午5:37:04 
* @version 1.0 
* @parameter   
* @return  
*/
public abstract class Implement {
	
	public < T extends FanInterface  > T  createFan( T t1 , Class< ? extends FanInterface > classw  ){
		System.out.println( " 获得类：" + t1.getClass().getName() );
		
		return t1;
	} 
	
}
