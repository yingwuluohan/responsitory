package com.java.statck;

/**
 * Created by yingwuluohan on 2017/8/28.
 */
public class StatckTest {
    public static void main(String[] args) {
        int num = 140;
        int n = 9;
        MyStack<Integer> myStack = new MyLinkedStack<Integer>();
        Integer result = num;
        while (true) {
            // 将余数入栈
            myStack.push(result % n);
            result = result / n;
            if (result == 0) {
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        // 按出栈的顺序倒序排列即可
        while ((result = myStack.pop()) != null) {
            sb.append(result);
        }
        System.out.println( sb.toString());
    }

}
