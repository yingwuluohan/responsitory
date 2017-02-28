package com.fang.common.project.schedul;

import com.fang.common.project.redis.Constants;
import com.fang.common.project.redis.RedisInstInfo;
import com.fang.common.project.redis.Router;

import java.util.concurrent.*;

/**
 * Created by yaopeng on 2015/11/4.
 */
public class HealthChecker {

    public static HealthChecker inst = new HealthChecker();

    /**
     * 负责调度
     */
    private ScheduledExecutorService healthCheckTimer;
    private ScheduledFuture healthCheckTimerFuture;
    /**
     * 负责执行具体的检查任务
     */
    private ExecutorService executor;

    private HealthChecker(){
        healthCheckTimer = Executors.newScheduledThreadPool(1);
        executor = new ThreadPoolExecutor(Constants.EXECUTOR_POOL_CORE_SIZE, Constants.EXECUTOR_POOL_MAX_SIZE, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(Constants.EXECUTOR_POOL_ARRAY_QUEUE_SIZE), new ThreadPoolExecutor.DiscardPolicy());
    }

    public void start(){
        healthCheckTimerFuture = healthCheckTimer.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                //master
                HealthCheckTask task = new HealthCheckTask(Router.inst.getMaster());
                executor.submit(task);

                for(RedisInstInfo r : Router.inst.getLocalSlavesInfoList()){
                    HealthCheckTask t = new HealthCheckTask(r.getAddressAndPort());
                    executor.submit(t);
                }
                for(RedisInstInfo r : Router.inst.getOtherSlavesInfoList()){
                    HealthCheckTask t = new HealthCheckTask(r.getAddressAndPort());
                    executor.submit(t);
                }
            }

        }, 0, Constants.HEALTH_CHECK_INTERVAL, TimeUnit.SECONDS);
    }

    public void stop(){
        healthCheckTimerFuture.cancel(true);
    }
}
