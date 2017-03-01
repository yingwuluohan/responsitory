package com.java.jdk8;

import java.util.ArrayList;
import java.util.List;


public class Optional1_8Jdk {
	/**
	 * Optional.of(T)：获得一个Optional对象，其内部包含了一个非null的T数据类型实例，若T=null，则立刻报错。
　　Optional.absent()：获得一个Optional对象，其内部包含了空值
　　Optional.fromNullable(T)：将一个T的实例转换为Optional对象，T的实例可以不为空，
	也可以为空[Optional.fromNullable(null)，和Optional.absent()等价。

	Optional1_8Jdk empty =  null;
	 private Optional optional;
	public Optional1_8Jdk(Optional optional){
		this.optional = optional;
	}
	//调用工厂方法创建Optional实例
	static Optional<String> name = Optional.of("Sanaulla");
	static void getInfo(){
		//map方法执行传入的lambda表达式参数对Optional实例的值进行修改。
		//为lambda表达式的返回值创建新的Optional实例作为map方法的返回值。
		Optional<String> upperName = name.map((value) -> value.toUpperCase());
		System.out.println(upperName.orElse("No value found")); 
		List< String > list = new ArrayList<>();
		list.add( "testcontroller" );
		
	}
	
	public static void main( String[] agrs ){
//		System.out.println( name );
		getInfo();
	}

	 */
	
	
	
}
