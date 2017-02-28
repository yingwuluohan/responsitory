package com.fang.common.project.schedul;

import com.alibaba.fastjson.JSON;
import com.fang.common.project.redis.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

/**
 * health check<br/>
 * 依赖jedis本身的getResource方法的异常，和redis的PING命令做检查<br/>
 * 检查过程中：连续n次检查失败，则判定为不可用。如果之前为不可用，下一次检查成功，则判定为可用<br/>
 */
public class HealthCheckTask implements Runnable {
    private final Log kafkaLogger = LogFactory.getLog("brokenMonitorLog");

    private String instanceAddress;

    public HealthCheckTask(String instanceAddress) {
        this.instanceAddress = instanceAddress;
    }

    @Override
    public void run() {
        checkAlive(this.instanceAddress);
    }

    private void checkAlive(String address){
        KooPool kooPool = Router.inst.getPool(address);
        Pool checkPool = Router.inst.getPoolForHealthCheck(address);
        try {
            String result = pingForInternel(checkPool, address);
            if(result.equalsIgnoreCase("PONG")){
                doWhenAvailable(kooPool,address);
            }else{
                doWhenUnavailable(kooPool,address);
            }
        } catch (Exception e) {
            doWhenUnavailable(kooPool,address);
        }
    }

    private void doWhenAvailable(KooPool kooPool,String address){
        kooPool.setStateOk();
        kooPool.resetErrorTimes();
    }

    private void doWhenUnavailable(KooPool kooPool,String address){
        kooPool.addErrorTimes();
        if(kooPool.getErrorTimes() == Constants.HEALTH_CHECK_ERROR_TIMES){
            if(kooPool.getState() == State.OK){
                RedisInstInfo info = Router.inst.getInfoByAddr(address);
                String errMsg = "pool state change:from OK to ERROR." + Constants.APPLOCATION + "=" + Router.inst.getAppRoom() + ";room=" + info.getRoom() + ",role=" + info.getRole() + ",address=" + info.getAddressAndPort();
                String s = newLogMessage("com.koolearn.framework.redis.route.HealthCheckTask", errMsg, "doWhenUnavailable", info.getAddressAndPort(), info.getAddressAndPort());
                kafkaLogger.warn(s);
            }
            kooPool.setStateError();
        }
    }

    /**
     * 检查redis可用性的核心
     * @param pool
     * @return
     * @throws JedisException
     */
    private String pingForInternel(Pool pool, String address) {
        Jedis jedis = null;
        try {
            //这里可能会抛异常：连接数达到上限，或者网络或redis本身挂了
            jedis = (Jedis) pool.getResource();
            //这里可能会抛异常：网络或redis本身挂了，或者由于网络慢到达timeout上限
            String ret = jedis.ping();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    private String newLogMessage(String className,String errMsg,String method,String requestIP,String localIP){
        LogMessage logMessage = new LogMessage();
        logMessage.setClassName(className);
        logMessage.setErrMsg(errMsg);
        logMessage.setTime(System.currentTimeMillis());
        logMessage.setMethod(method);
        logMessage.setRequestIP(requestIP);
        logMessage.setLocalIP(localIP);
        logMessage.setType(Constants.SQL_CONNECTION_ERROR);
        return JSON.toJSONString(logMessage);
    }
}
