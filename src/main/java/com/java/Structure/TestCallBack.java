package com.java.Structure;

public class TestCallBack {
	public static void main( String[] args ){
		Employee em = new Employee();
		em.setCallBack( new Boss() );
		em.doSome();
	}
}
