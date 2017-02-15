package com.java.threadPool;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.eventbus.DeadEvent;
import com.google.common.util.concurrent.Uninterruptibles;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** 
* @author  dfn  : 
* @date 创建时间：2016年3月8日 下午3:53:59 
* @version 1.0 
* @parameter   
* @return  
*/
public class ThreadPoolHandler {
    private final SetMultimap<Class<?>, EventSubscriber> subscribersByType = HashMultimap.create();
    private final ReadWriteLock subscribersByTypeLock = new ReentrantReadWriteLock();
    private Executor timeoutMonitor = Executors.newSingleThreadExecutor(new VaporThreadFactory(this.getClass().getName() + "-TimeoutMonitor", true));
	 private final ExecutorService executor ;
	 private static final Object VOID = new Object();
	 public ThreadPoolHandler(ExecutorService executor){
		 this.executor = executor;
	 }
	 
	 public void post(Object event) {
	        Set dispatchTypes = new HashSet();
	        boolean dispatched = false;
	        Iterator var4 = dispatchTypes.iterator();

	        while(var4.hasNext()) {
	            Class eventType = (Class)var4.next();
	            this.subscribersByTypeLock.readLock().lock();

	            try {
	                Set wrappers = this.subscribersByType.get(eventType);
	                if(!wrappers.isEmpty()) {
	                    dispatched = true;
	                    Iterator var7 = wrappers.iterator();

	                    while(var7.hasNext()) {
	                        EventSubscriber wrapper = (EventSubscriber)var7.next();
	                        ThreadPoolHandler.EventWithSubscriber task = new ThreadPoolHandler.EventWithSubscriber(event, wrapper);


	                        Future future = this.executor.submit(task);
	                        this.timeoutMonitor.execute(new ThreadPoolHandler.TimeoutMonitor(future, task));
	                    }
	                }
	            } finally {
	                this.subscribersByTypeLock.readLock().unlock();
	            }
	        }

	        if(!dispatched && !(event instanceof DeadEvent)) {
	            this.post(new DeadEvent(this, event));
	        }

	    }
	 private class TimeoutMonitor implements Runnable {
	        private final Future<Object> future = null;
	        private final ThreadPoolHandler.EventWithSubscriber task = null;

	    

	        public TimeoutMonitor(Future future2, EventWithSubscriber task2) {
				// TODO Auto-generated constructor stub
			}



			public void run() {
	            try {
	                Uninterruptibles.getUninterruptibly(this.future, 1L, TimeUnit.MINUTES);
	            } catch ( Exception e ) {
	            }

	        }
	    }
	 class EventWithSubscriber implements Callable<Object> {
	        final Object event;
	        final EventSubscriber subscriber;

	        public EventWithSubscriber(Object event, EventSubscriber subscriber) {
	            this.event = Preconditions.checkNotNull(event);
	            this.subscriber = (EventSubscriber)Preconditions.checkNotNull(subscriber);
	        }

	        public Object call() throws Exception {
	            try {
	                this.subscriber.handleEvent(this.event);
	            } catch (InvocationTargetException var2) {
	            }

	            return VOID;
	        }

	        public String toString() {
	            return MoreObjects.toStringHelper(this).add("event", this.event).add("subscriber", this.subscriber).toString();
	        }
	    }
	 
	 
	 
	 
	 
}
