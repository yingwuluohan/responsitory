package com.google;

import com.google.common.util.concurrent.RateLimiter;

import java.util.List;
import java.util.concurrent.Executor;

/**
 *
 * 令牌桶限流方法
 * Created by yingwuluohan on 2018/2/27.
 */
public class TokenLimiter {

    //速率是每秒12个许可
    final RateLimiter rateLimiter = RateLimiter.create(12.0);



    void submitTasks(List<TaskThread > tasks, Executor executor) {
        for (Runnable task : tasks) {
            rateLimiter.acquire(); // 也许需要等待
            executor.execute(task);
        }
    }




}
