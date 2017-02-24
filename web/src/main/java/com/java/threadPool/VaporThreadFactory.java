package com.java.threadPool;
/** 
* @author  dfn  : 
* @date 创建时间：2016年3月8日 下午4:43:19 
* @version 1.0 
* @parameter   
* @return  
*/

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class VaporThreadFactory implements ThreadFactory {
    private final boolean daemon;
    private AtomicInteger threadCount = new AtomicInteger(0);
    private final String name;

    public VaporThreadFactory(String name, boolean daemon) {
        this.name = name;
        this.daemon = daemon;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, this.name + "-" + this.threadCount.getAndIncrement());
        t.setDaemon(this.daemon);
        return t;
    }
}

