package com.fang.common.project.redis;

import com.exception.InitialisationException;
import com.heatbeat.HeartbeatManager;
import com.heatbeat.Status;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * Created by fn on 2017/2/27.
 */
public class GenericObjectPool extends org.apache.commons.pool.impl.GenericObjectPool implements ObjectPool, Initialisable {
    private boolean isValid;
    private boolean enable;
    private String name;
    private GenericObjectPool.GenericHeartbeatDelayed delay;

    public GenericObjectPool() {
        this((PoolableObjectFactory)null, 8, (byte)1, -1L, 8, 0, false, false, -1L, 3, 1800000L, false);
    }

    public GenericObjectPool(PoolableObjectFactory factory) {
        this(factory, 8, (byte)1, -1L, 8, 0, false, false, -1L, 3, 1800000L, false);
    }

    public GenericObjectPool(PoolableObjectFactory factory, int maxActive) {
        this(factory, maxActive, (byte)1, -1L, 8, 0, false, false, -1L, 3, 1800000L, false);
    }

    public GenericObjectPool(PoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait) {
        this(factory, maxActive, whenExhaustedAction, maxWait, 8, 0, false, false, -1L, 3, 1800000L, false);
    }

    public GenericObjectPool(PoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, boolean testOnBorrow, boolean testOnReturn) {
        this(factory, maxActive, whenExhaustedAction, maxWait, 8, 0, testOnBorrow, testOnReturn, -1L, 3, 1800000L, false);
    }

    public GenericObjectPool(PoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle) {
        this(factory, maxActive, whenExhaustedAction, maxWait, maxIdle, 0, false, false, -1L, 3, 1800000L, false);
    }

    public GenericObjectPool(PoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, boolean testOnBorrow, boolean testOnReturn) {
        this(factory, maxActive, whenExhaustedAction, maxWait, maxIdle, 0, testOnBorrow, testOnReturn, -1L, 3, 1800000L, false);
    }

    public GenericObjectPool(PoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictionRunsMillis, int numTestsPerEvictionRun, long minEvictableIdleTimeMillis, boolean testWhileIdle) {
        this(factory, maxActive, whenExhaustedAction, maxWait, maxIdle, 0, testOnBorrow, testOnReturn, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, minEvictableIdleTimeMillis, testWhileIdle);
    }

    public GenericObjectPool(PoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, int minIdle, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictionRunsMillis, int numTestsPerEvictionRun, long minEvictableIdleTimeMillis, boolean testWhileIdle) {
        super(factory, maxActive, whenExhaustedAction, maxWait, maxIdle, minIdle, testOnBorrow, testOnReturn, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, minEvictableIdleTimeMillis, testWhileIdle, -1L);
        this.isValid = true;
        this.delay = new GenericObjectPool.GenericHeartbeatDelayed(3L, TimeUnit.SECONDS, this);
    }

    public GenericObjectPool(PoolableObjectFactory factory, int maxActive, byte whenExhaustedAction, long maxWait, int maxIdle, int minIdle, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictionRunsMillis, int numTestsPerEvictionRun, long minEvictableIdleTimeMillis, boolean testWhileIdle, long softMinEvictableIdleTimeMillis) {
        //super(factory, maxActive, whenExhaustedAction, maxWait, maxIdle, minIdle, testOnBorrow, testOnReturn, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, minEvictableIdleTimeMillis, testWhileIdle, softMinEvictableIdleTimeMillis, true);
        this.isValid = true;
        this.delay = new GenericObjectPool.GenericHeartbeatDelayed(3L, TimeUnit.SECONDS, this);
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean isEnabled) {
        this.enable = isEnabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object borrowObject() throws Exception {
        if(!this.isValid && this.getNumActive() > 0 && this.getNumIdle() == 0) {
            throw new NoSuchElementException("poolName=" + this.name + ", pool is invalid");
        } else {
            try {
                return super.borrowObject();
            } catch (Exception var2) {
                this.isValid = false;
                throw var2;
            }
        }
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    public void init() throws InitialisationException {
        HeartbeatManager.addHeartbeat(this.delay);
    }

    public void close() throws Exception {
        super.close();
        HeartbeatManager.removeHeartbeat(this.delay);
    }

    public boolean validate() {
        Object object = null;

        boolean e1;
        try {
            object = super.borrowObject();
            this.setValid(true);
            boolean e = true;
            return e;
        } catch (Exception var13) {
            this.setValid(false);
            e1 = false;
        } finally {
            if(object != null) {
                try {
                    this.returnObject(object);
                } catch (Exception var12) {
                    ;
                }
            }

        }

        return e1;
    }

    public static class GenericHeartbeatDelayed extends com.fang.common.project.redis.ObjectPool.ObjectPoolHeartbeatDelayed {
        public GenericHeartbeatDelayed(long nsTime, TimeUnit timeUnit, ObjectPool pool) {
            super(nsTime, timeUnit, (com.fang.common.project.redis.ObjectPool) pool);
        }

        public Status doCheck() {
            return super.doCheck();
        }

        public boolean isCycle() {
            return true;
        }
    }
}
