package com.fang.common.project.redis;

import com.heatbeat.HeartbeatDelayed;
import com.heatbeat.Status;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * Created by  on 2017/2/27.
 */
public interface ObjectPool extends org.apache.commons.pool.ObjectPool {
    Logger logger = Logger.getLogger(ObjectPool.class);

    boolean isEnable();

    void setEnable(boolean var1);

    boolean isValid();

    void setValid(boolean var1);

    boolean validate();

    String getName();

    void setName(String var1);

    public static class ObjectPoolHeartbeatDelayed extends HeartbeatDelayed {
        private ObjectPool pool;

        public boolean isCycle() {
            return false;
        }

        public ObjectPool getPool() {
            return this.pool;
        }

        public ObjectPoolHeartbeatDelayed(long nsTime, TimeUnit timeUnit, ObjectPool pool) {
            super(nsTime, timeUnit);
            this.pool = pool;
        }

        public boolean equals(Object obj) {
            if(!(obj instanceof ObjectPool.ObjectPoolHeartbeatDelayed)) {
                return false;
            } else {
                ObjectPool.ObjectPoolHeartbeatDelayed other = (ObjectPool.ObjectPoolHeartbeatDelayed)obj;
                return other.pool == this.pool && this.getClass() == obj.getClass();
            }
        }

        public int hashCode() {
            return this.pool == null?this.getClass().hashCode():this.getClass().hashCode() + this.pool.hashCode();
        }

        public Status doCheck() {
            if(this.pool.validate()) {
                this.pool.setValid(true);
                return Status.VALID;
            } else {
                this.pool.setValid(false);
                return Status.INVALID;
            }
        }

        public String getName() {
            return this.pool.getName();
        }
    }

    public static class ActiveNumComparator implements Comparator<ObjectPool> {
        public ActiveNumComparator() {
        }

        public int compare(ObjectPool o1, ObjectPool o2) {
            return o1.getNumActive() - o2.getNumActive();
        }
    }
}
