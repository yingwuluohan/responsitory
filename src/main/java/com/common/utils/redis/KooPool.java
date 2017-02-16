package com.common.utils.redis;

import redis.clients.util.Pool;

/**
 * Created by yaopeng on 2015/11/3.
 */
public class KooPool {
    private Pool pool;
    private String addressAndPort;
    private State state;
    /**
     * 累积错误次数，恢复可用后需要重置为0
     */
    private int errorTimes = 0;

    public int getErrorTimes() {
        return errorTimes;
    }

    public void addErrorTimes(){
        this.errorTimes = this.errorTimes + 1;
    }

    public void resetErrorTimes() {
        this.errorTimes = 0;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public State getState() {
        return state;
    }

    public void setStateOk() {
        this.state = State.OK;
    }

    public void setStateError(){
        this.state = State.ERROR;
    }

    public String getAddressAndPort() {
        return addressAndPort;
    }

    public void setAddressAndPort(String addressAndPort) {
        this.addressAndPort = addressAndPort;
    }

}
