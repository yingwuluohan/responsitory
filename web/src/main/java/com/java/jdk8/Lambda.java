package com.java.jdk8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lambda  {

    public static void test(String str ){
        // 1. 不需要参数,返回值为 5
//		() -> 5;
        String[] atp = { "3sdf" , "45sdf","sdf","12"};
        List<String> players =  Arrays.asList(atp);
        // 以前的循环方式
//		for (String player : players) {
//		     System.out.print(player + "; ");
//		}
        // 使用 lambda 表达式以及函数操作(functional operation)
      //  players.forEach((player1) -> System.out.print(player1 + "; "));
     //   players.stream().map( s -> s.substring( 1 ) );
//		players.stream().filter("");
        String[] array = ( String[] )players.toArray();
    }
    public static Map< String , String > get(String str ){
        System.out.println( "Lambda"  + str );
        Map< String , String > map = new HashMap();
        map.put( str, str);
        return map;
    }
    public static String getMap( String key , Map map ){
        return ( String )map.get( key );
    }



    public static void main( String[] args ){
        test( "ste");
    }

    public Lambda getInfo() {
        // TODO Auto-generated method stub
        return null;
    }


}
