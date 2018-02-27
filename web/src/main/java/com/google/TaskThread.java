package com.google;/**
 * Created by Admin on 2018/2/26.
 */

/**
 * @company: 东方
 * @author: FANGNAN
 * @date: 2018/2/26
 **/
public class TaskThread implements Runnable {

    private int id;

    public TaskThread(int id) {
        this.id = id;
    }

    @Override
    public void run(){

        System.out.println( "当前id:" + id );
    }



}
