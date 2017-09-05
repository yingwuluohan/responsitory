package com.java.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by fn on 2017/5/22.
 */
public class TestJava {
<<<<<<< HEAD
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

        String str = new String( "dfr可以,dj38" );
        char[] array = str.toCharArray();
        for ( char b : array ){
            System.out.println( b );
        }
        Integer[] a = new Integer[] { 6, 3, 9, 3, 2, 4, 5, 7 };
        Integer[] b = new Integer[] { 5, 8, 6, 2, 1, 9 };
        List _a = Arrays.asList(a);
        List _b = Arrays.asList(b);
        Collection realA = new ArrayList<Integer>(_a);
        Collection realB = new ArrayList<Integer>(_b);
        // 求交集
        realA.retainAll(realB);
        System.out.println( realA );

    }
=======
>>>>>>> bd9f33305c8cf1f1afdc5d63a30f9a863ad048ad


}
