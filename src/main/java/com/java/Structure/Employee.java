package com.java.Structure;

public class Employee {
	private CallBackInterface callBack = null;
	
	//告诉老板的联系方式，即注册
	public void setCallBack( CallBackInterface callBack ){
		this.callBack = callBack;
	}
	//工人干活
	public void doSome(){
		//干活
		for(int i = 1 ; i < 10 ;i++ ){
			System.out.println( "第【"+i+"】事情干完了" );
		}
		callBack.execute();
	}
	
}
