package com.java.moreThread.demo_026;

import java.util.concurrent.*;

/**
 * 认识future(将来)
 * @author Jcon
 *
 */
public class T06_Future {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		FutureTask<Integer> task = new FutureTask<>(()->{
			TimeUnit.MILLISECONDS.sleep(500);
			return 1000;
		}) ;//相当于new Callable(){Integer call();}
		
		new Thread(task).start();
		
		System.out.println(task.get()); //阻塞
		
		ExecutorService service = Executors.newFixedThreadPool(5);
		Future<Integer> f = service.submit(()->{
			TimeUnit.MILLISECONDS.sleep(500);
			return 1;
		});
		
		System.out.println(f.get());
		System.out.println(f.isDone());
	}
}
