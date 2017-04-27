package com.fang.common.project.schedul;

import com.fang.common.project.redis.Constants;

import java.util.concurrent.*;

/**
 * Created by on 2017/4/27.
 * 1.无界队列。使用无界队列（例如，不具有预定义容量的 LinkedBlockingQueue）将导致在所有 corePoolSize
 *  线程都忙时新任务在队列中等待。这样，创建的线程就不会超过 corePoolSize。（因此，maximumPoolSize 的值也就无效了。）
 *  当每个任务完全独立于其他任务，即任务执行互不影响时，适合于使用无界队列；例如，在 Web 页服务器中。
 *  这种排队可用于处理瞬态突发请求，当命令以超过队列所能处理的平均数连续到达时，此策略允许无界线程具有增长的可能性。
   2.有界队列。当使用有限的 maximumPoolSizes 时，有界队列（如 ArrayBlockingQueue）有助于防止资源耗尽，
    但是可能较难调整和控制。队列大小和最大池大小可能需要相互折衷：使用大型队列和小型池可以最大限度地降低
    CPU 使用率、操作系统资源和上下文切换开销，但是可能导致人工降低吞吐量。如果任务频繁阻塞（例如，如果它们是 I/O 边界），
    则系统可能为超过您许可的更多线程安排时间。使用小型队列通常要求较大的池大小，CPU 使用率较高，
    但是可能遇到不可接受的调度开销，这样也会降低吞吐量
 */
public class SystemCheckRunner {

    public void init() {
        system.start();
    }

    private static SystemCheckRunner system = new SystemCheckRunner();
    public SystemCheckRunner(){
        scheduledExecutorThread = Executors.newScheduledThreadPool(2);//单线程
        executor = new ThreadPoolExecutor( 10 ,20 , 0L, TimeUnit.SECONDS,
                   new ArrayBlockingQueue<Runnable>( 10000 ),
                   new ThreadPoolExecutor.DiscardPolicy()
                   );
    }
    /**
     * 负责调度
     * 任务调度器：schedule()方法第一个参数是任务实例，第二个参数是延迟时间，第三个是时间单元
     * 如果配置ScheduledThreadPoolExecutor为单线程，则与使用Timer等效
     */
    private ScheduledExecutorService scheduledExecutorThread;
    /**
        表示ScheduledExecutorService中提交了任务的返回结果。我们通过Delayed的接口getDelay()方法知道该任务还有好久才被执行。
     *
     */
    private ScheduledFuture scheduledFuture;
    /**
     * 负责执行具体的检查任务
     */
    private ExecutorService executor;
    public void start(){
        scheduledFuture = scheduledExecutorThread.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println( " 当前线程：" + Thread.currentThread().getName()  + ":" + System.currentTimeMillis()  );
            }
        }, 0,  10 , TimeUnit.SECONDS );
    }
    public void stop(){
        scheduledFuture.cancel(true);
    }
}
