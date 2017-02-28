package com.fang.common.project.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Router {
    private final Log log = LogFactory.getLog(Router.class);

    public static Router inst = new Router();

    private RedisInstInfo masterInfo;

    /**
     * 本机房redis读库列表
     */
    private List<RedisInstInfo> localSlavesInfoList = new ArrayList<RedisInstInfo>();
    /**
     * 其它机房redis读库列表
     */
    private List<RedisInstInfo> otherSlavesInfoList = new ArrayList<RedisInstInfo>();
    private String appRoom;
    private JedisPoolConfig poolConfig;
    private int timeout;
    private String password;
    private int database;

    private ConcurrentHashMap<String, KooPool> dataSourceForUser = new ConcurrentHashMap<String, KooPool>();
    private ConcurrentHashMap<String, Pool> dataSourceForHealthCheck = new ConcurrentHashMap<String, Pool>();
    private ConcurrentHashMap<Pool,String> dataSourceAndAddressMap = new ConcurrentHashMap<Pool, String>();

    private Router(){}

    public void init(String addressStr, JedisPoolConfig poolConfig, int timeout, String password, int database){
        this.appRoom = System.getProperty(Constants.APPLOCATION, Constants.NONE);
        parseAddress(addressStr);
        this.poolConfig = poolConfig;
        this.timeout = timeout;
        this.password = password;
        this.database = database;

        addDataSource(this.masterInfo.getAddressAndPort(), this.poolConfig, this.timeout, this.password, this.database);
        for(RedisInstInfo r : localSlavesInfoList){
            addDataSource(r.getAddressAndPort(), this.poolConfig, this.timeout, this.password, this.database);
        }
        for(RedisInstInfo r : otherSlavesInfoList){
            addDataSource(r.getAddressAndPort(), this.poolConfig, this.timeout, this.password, this.database);
        }
    }

    public void destroy(){
        for(KooPool kooPool : dataSourceForUser.values()){
            kooPool.getPool().destroy();
        }
        for(Pool pool : dataSourceForHealthCheck.values()){
            pool.destroy();
        }
    }

    public void addDataSource(String address, JedisPoolConfig poolConfig, int timeout, String password, int database){
        addDataSourceForUser(address, poolConfig, timeout, password, database);
        addDataSourceForHealthCheck(address, password, database);
    }

    private void addDataSourceForUser(String address, JedisPoolConfig poolConfig, int timeout, String password, int database){
        String[] addressArr = address.split(":");
        Pool jedisPool = null;//new JedisPool(poolConfig, addressArr[0], Integer.parseInt(addressArr[1]),timeout,password,database);
        KooPool pool = new KooPool();
        pool.setPool(jedisPool);
        pool.setStateOk();
        pool.setAddressAndPort(address);
        dataSourceForUser.put(address, pool);
        dataSourceAndAddressMap.put(jedisPool,address);
    }

    private void addDataSourceForHealthCheck(String address, String password, int database){
        String[] addressArr = address.split(":");
        JedisPoolConfig config = new JedisPoolConfig();
       // config.setMaxActive(5);
        config.setMaxIdle(1);
       // config.setMaxWait(1000);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(true);
        Pool pool = new JedisPool(config,addressArr[0], Integer.parseInt(addressArr[1]), Protocol.DEFAULT_TIMEOUT, password,database);
        dataSourceForHealthCheck.put(address, pool);
    }

    public String getAddressAndPortByPool(Pool pool){
        return dataSourceAndAddressMap.get(pool);
    }

    /**
     * 返回写连接池
     * @return
     */
    public Pool getWritePool(){
        return getPoolForOneRedisCase(Constants.WRITE);
    }

    /**
     * 返回读连接池<br/>
     * 目标是优先选择本机房的
     * @return
     */
    public Pool getReadPool(){
        //只有1个redis时，读写指向的是同一个
        if(isOnlyOneRedis()){
            return getPoolForOneRedisCase(Constants.READ);
        }else{
            //如果应用没设置机房
            if(appRoom.equals(Constants.NONE)){
                List<RedisInstInfo> total = new ArrayList<RedisInstInfo>();
                total.addAll(localSlavesInfoList);
                total.addAll(otherSlavesInfoList);
                Pool randomRedis = randomRedis(total);
                if(randomRedis != null){
                    return randomRedis;
                }else{
                    String errMsg = "no avaible redis instance for read!switch to master for read";
                    log.warn(errMsg);
                    return getPoolForOneRedisCase(Constants.READ);
                }
            }else{
                Pool localRoomRedis = randomRedis(localSlavesInfoList);
                //本机房有可用
                if(localRoomRedis != null){
                    return localRoomRedis;
                //本机房没有可用的了
                }else{
                    Pool otherRoomRedis = randomRedis(otherSlavesInfoList);
                    if(otherRoomRedis != null){
                        return otherRoomRedis;
                    }else{
                        //全都挂了
                        String errMsg = "no avaible redis instance for read!switch to master for read";
                        log.warn(errMsg);
                        return getPoolForOneRedisCase(Constants.READ);
                    }
                }
            }
        }
    }

    public Pool getPoolForOneRedisCase(String readOrWrite){
        String addr = masterInfo.getAddressAndPort();
        KooPool pool = getPool(addr);
        if(pool.getState() == State.ERROR){
            RedisInstInfo info = Router.inst.getInfoByAddr(addr);
            String errMsg = "redis for " +readOrWrite + " current not available,but only 1 redis instance or 1 slave redis,so continue." + Constants.APPLOCATION + "=" + Router.inst.getAppRoom() + ";room=" + info.getRoom() + ",role=" + info.getRole() + ",address=" + info.getAddressAndPort();
            log.warn(errMsg);
        }
        return pool.getPool();
    }

    private boolean isOnlyOneRedis(){
        if(localSlavesInfoList.size() == 0 && otherSlavesInfoList.size() == 0){
            return true;
        }else{
            return false;
        }
    }

    public String getMaster() {
        return masterInfo.getAddressAndPort();
    }

    public KooPool getPool(String address){
        return dataSourceForUser.get(address);
    }

    public Pool getPoolForHealthCheck(String address){
        return dataSourceForHealthCheck.get(address);
    }

    private void parseAddress(String addressStr){
        if(addressStr == null || addressStr.trim().length() == 0){
            JedisException e = new JedisException("address cannot be empty!");
            throw e;
        }else{
            //兼容老配置，如:10.155.20.184:6379
            if(!addressStr.trim().contains("/")){
                masterInfo = new RedisInstInfo(Constants.NONE, Constants.NONE,addressStr);
                return;
            }

            //新配置
            int masterCount = 0;
            String[] array = addressStr.trim().split(",");
            for(String ele : array){
                String[] sections = ele.trim().split("\\/");
                if(sections == null || sections.length != 3){
                    JedisException e = new JedisException("address format is not valid!");
                    throw e;
                }else{
                    String room = sections[0];
                    String role = sections[1];
                    String addressAndPort = sections[2];

                    if(role.equalsIgnoreCase("master")){
                        masterCount++;
                    }
                    if(masterCount > 1){
                        JedisException e = new JedisException("master count must be 1!");
                        throw e;
                    }else{
                        RedisInstInfo r = new RedisInstInfo(room,role,addressAndPort);
                        if(role.equalsIgnoreCase("master")){
                            this.masterInfo = r;
                        }else{
                            if(room.equalsIgnoreCase(appRoom)){
                                localSlavesInfoList.add(r);
                            }else{
                                otherSlavesInfoList.add(r);
                            }
                        }
                    }
                }
            }
        }
    }

    public List<RedisInstInfo> getLocalSlavesInfoList() {
        return localSlavesInfoList;
    }

    public List<RedisInstInfo> getOtherSlavesInfoList() {
        return otherSlavesInfoList;
    }

    public RedisInstInfo getInfoByAddr(String addr){
        if(addr.equals(masterInfo.getAddressAndPort())) return masterInfo;
        for(RedisInstInfo r : localSlavesInfoList){
            if(addr.equals(r.getAddressAndPort()))  return r;
        }
        for(RedisInstInfo r : otherSlavesInfoList){
            if(addr.equals(r.getAddressAndPort()))  return r;
        }
        return null;
    }

    public String getAppRoom() {
        return appRoom;
    }

    private String newLogMessage(String className,String errMsg,String method,String requestIP,String localIP){
        LogMessage logMessage = new LogMessage();
        logMessage.setClassName(className);
        logMessage.setErrMsg(errMsg);
        logMessage.setTime(System.currentTimeMillis());
        logMessage.setMethod(method);
        logMessage.setRequestIP(requestIP);
        logMessage.setLocalIP(localIP);
        return JSON.toJSONString(logMessage);
    }

    /**
     * 随机选择可用的机房；超过1万次随机后，仍找不到，返回null
     * @param list
     * @return
     */
    private Pool randomRedis(List<RedisInstInfo> list){
        if(list.size() == 0)    return null;

        Random rand = new Random();
        for(int i = 0; i < Constants.MAX_RANDOM_TIMES; i++){
            RedisInstInfo selected = list.get(rand.nextInt(list.size()));
            KooPool kooPool = getPool(selected.getAddressAndPort());
            if(State.OK == kooPool.getState()){
                return kooPool.getPool();
            }
        }
        return null;
    }
}
