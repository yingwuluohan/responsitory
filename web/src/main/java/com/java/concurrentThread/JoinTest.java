package com.java.concurrentThread;

/**
 * Created by Admin on 2017/11/14.
 */
public class JoinTest implements Runnable{
    @Override
    public void run() {

        try {
            System.out.println(Thread.currentThread().getName() + " start-----");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " end------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i=0;i<5;i++) {
            Thread test = new Thread(new JoinTest());
            test.start();
        }

        System.out.println("Finished~~~");
    }
}
