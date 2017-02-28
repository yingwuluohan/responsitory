package com.heatbeat;


import com.fang.common.project.redis.GenericObjectPool;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

/**
 * Created by fn on 2017/2/27.
 */
public class HeartbeatManager {
    static Logger logger = Logger.getLogger(HeartbeatManager.class);
    protected static final BlockingQueue<HeartbeatDelayed> HEART_BEAT_QUEUE = new DelayQueue();

    public HeartbeatManager() {
    }

    public static void addHeartbeat(HeartbeatDelayed delay) {
        if(!HEART_BEAT_QUEUE.contains(delay)) {
            HEART_BEAT_QUEUE.offer(delay);
        }
    }

    public static void removeHeartbeat( HeartbeatDelayed delay) {
        HEART_BEAT_QUEUE.remove(delay);
    }

    static {
        (new Thread() {
            {
                this.setDaemon(true);
                this.setName("HeartbeatManagerThread");
            }

            public void run() {
                HeartbeatDelayed delayed = null;

                while(true) {
                    while(true) {
                        try {
                            delayed = (HeartbeatDelayed)HeartbeatManager.HEART_BEAT_QUEUE.take();
                            Status e = delayed.doCheck();
                            if(HeartbeatManager.logger.isDebugEnabled()) {
                                HeartbeatManager.logger.debug("checked task taskName=" + delayed.getName() + " ,Status=" + e);
                            }

                            if(delayed.isCycle()) {
                                delayed.reset();
                                HeartbeatManager.addHeartbeat(delayed);
                            } else if(e == Status.INVALID) {
                                delayed.reset();
                                HeartbeatManager.addHeartbeat(delayed);
                            } else {
                                delayed.cancel();
                            }
                        } catch (Exception var3) {
                            HeartbeatManager.logger.error("check task error", var3);
                        }
                    }
                }
            }
        }).start();
    }
}
