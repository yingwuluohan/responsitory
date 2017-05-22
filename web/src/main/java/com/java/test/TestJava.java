package com.java.test;

/**
 * Created by fn on 2017/5/22.
 */
public class TestJava {
    /**
     * 或运算，
     * 5转换为二进制：0000 0000 0000 0000 0000 0000 0000 0101
       3转换为二进制：0000 0000 0000 0000 0000 0000 0000 0011
     -------------------------------------------------------------------------------------
       7转换为二进制：0000 0000 0000 0000 0000 0000 0000 0111
     * @param args
     */
    public static void main( String[] args ){
        System.out.println( "位移：" + ( 1<< 3 ));// 1 * 2 * 2 * 2
        System.out.println( "位移：" + ( 1>> 2 ));
        System.out.println( "条件或 ：" + ( 1| 2 ));
        System.out.println( "条件或 ：" + ( 1| 1 ));

    }


}
