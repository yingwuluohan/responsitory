package com.java.fanxing;
/** 
* @author  dfn  : 
* @version 1.0
* @parameter   
* @return  
*/
public class Fanxing extends  Implement{
	public void getInfo() {

	}

	public void get(){
		super.createFan(new FanInterChild(), FanInterChild.class);
	}
	
	public static void main( String[] args ){
		Fanxing f = new Fanxing();
		f.get();
	}
	
}
