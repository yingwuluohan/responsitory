package com.fang.common.project;

import com.dianping.cat.message.Transaction;
import com.fang.common.project.redis.AbstractKooJedisClient;

import com.fang.common.project.redis.LogMessage;
import com.fang.common.project.redis.Router;
import com.fang.common.project.schedul.HealthChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;
import redis.clients.util.SafeEncoder;
import redis.clients.util.Slowlog;

import java.util.*;

public class KooJedisClient extends AbstractKooJedisClient {

   // private final Log log = LogFactory.getLog(AbstractKooJedisClient.class);

    @Override
    public void init() {
//        String[] addressArr = getAddress().split(":");
//
//        this.pool = new JedisPool(getConfig(), addressArr[0], Integer.parseInt(addressArr[1]),
//                getTimeOut() == 0 ? DEFAUlT_TIMEOUT : getTimeOut());
        Router.inst.init(getAddress(),getConfig(),getTimeOut() == 0 ? DEFAUlT_TIMEOUT : getTimeOut(), getPassword() == null ? null : getPassword(), getDatabase() == 0 ? DEFAUlT_DATABASE : getDatabase());
        HealthChecker.inst.start();
    }

    @Override
    public void destroy() {

        super.destroy();
    }

    /**
     * 
     * @title: zunionstore
     * @description: TODO(这里用一句话描述这个方法的作用)
     * @param @param dstkey
     * @param @param sets
     * @param @return 设定文件
     * @return Long 返回类型
     * @throws
     */
    public Long zunionstore(String dstkey, String sets) {
        dstkey = buildKey(dstkey);
        sets = buildKey(sets);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("zunionstore", dstkey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("zunionstore",dstkey,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.zunionstore(dstkey, sets);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        dstkey = buildKey(dstkey);
        if (sets == null) {
            return 0l;
        }
        String[] setArray = new String[sets.length];
        for (int i = 0; i < setArray.length; i++) {
            setArray[i] = buildKey(sets[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("zunionstore ZParams",dstkey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("zunionstore ZParams",dstkey,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.zunionstore(dstkey, params, setArray);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public Long zinterstore(String dstkey, String... sets) {
        dstkey = buildKey(dstkey);
        if (sets == null) {
            return 0l;
        }
        String[] setArray = new String[sets.length];
        for (int i = 0; i < setArray.length; i++) {
            setArray[i] = buildKey(sets[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("zinterstore", dstkey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("zinterstore",dstkey,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.zinterstore(dstkey, setArray);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        dstkey = buildKey(dstkey);
        if (sets == null) {
            return 0l;
        }
        String[] setArray = new String[sets.length];
        for (int i = 0; i < setArray.length; i++) {
            setArray[i] = buildKey(sets[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("zinterstore ZParams",dstkey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("zinterstore ZParams",dstkey,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.zinterstore(dstkey, params, setArray);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /*
     * public String watch(String[] keys) { Jedis jedis = null; try { jedis =
     * (Jedis) getPool().getResource(); return jedis.watch(keys); } catch
     * (Exception e) { log.error("redis error ", e); throw new
     * JedisException(e); } finally { getPool().returnResourceObject(jedis); } }
     * 
     * public String unwatch() { Jedis jedis = null; try { jedis = (Jedis)
     * getPool().getResource(); return jedis.unwatch(); } catch (Exception e) {
     * log.error("redis error ", e); throw new JedisException(e); } finally {
     * getPool().returnResourceObject(jedis); } }
     */

    /*
     * public void sync() { Jedis jedis = null; try { jedis = (Jedis)
     * getPool().getResource(); jedis.sync(); } catch (Exception e) {
     * log.error("redis error ", e); throw new JedisException(e); } finally {
     * getPool().returnResourceObject(jedis); } }
     */

    /**
     * 
     * @title: sunionstore
     * @description: 这个命令类似于 SUNION 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。如果
     *               destination 已经存在，则将其覆盖。
     * @param dstkey
     * @param keys
     * @return 结果集中的元素数量。
     * @throws
     */
    public Long sunionstore(String dstkey, String... keys) {
        dstkey = buildKey(dstkey);
        if (keys == null) {
            return 0l;
        }
        String[] setArray = new String[keys.length];
        for (int i = 0; i < setArray.length; i++) {
            setArray[i] = buildKey(keys[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("sunionstore", dstkey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sunionstore",dstkey,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.sunionstore(dstkey, setArray);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: sunion
     * @description: 返回一个集合的全部成员，该集合是所有给定集合的并集。不存在的 key 被视为空集。
     * @param keys
     * @return 并集成员的列表。
     * @throws
     */
    public Set<String> sunion(String... keys) {
        if (keys == null) {
            return null;
        }
        String[] setArray = new String[keys.length];
        for (int i = 0; i < setArray.length; i++) {
            setArray[i] = buildKey(keys[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("sunion", getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sunion",getKeyStr(keys),logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Set<String> ret = jedis.sunion(setArray);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    @SuppressWarnings("unchecked")
    public <E> Set<E> sunionE(Class<E> memberClass, String... keys) {
        if (keys == null) {
            return null;
        }
        String[] setArray = keys.clone();

        BinaryJedis commands = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("sunionE", getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sunionE",getKeyStr(keys),logMessage,pool);
        try {
            byte[][] bkey = new byte[keys.length][];
            for (int i = 0; i < bkey.length; i++) {
                bkey[i] = buildKey(setArray[i]).getBytes(Protocol.CHARSET);
            }
            commands = (BinaryJedis) pool.getResource();
            Set<byte[]> set = commands.sunion(bkey);
            pool.returnResourceObject(commands);
            if (set != null && set.size() != 0) {
                Set<E> result = new HashSet<E>();
                for (byte[] bt : set) {
                    result.add((E) serializer.deserialize(bt));
                }
                t.setStatus(Transaction.SUCCESS);
                return result;
            }
            t.setStatus(Transaction.SUCCESS);
            return null;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (commands != null) {
                pool.returnBrokenResource(commands);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (commands != null) {
                pool.returnResourceObject(commands);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            jedis.subscribe(jedisPubSub, channels);
            pool.returnResourceObject(jedis);
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    /**
     * 
     * @title: strlen
     * @description: 返回 key 所储存的字符串值的长度。
     * @param keyT
     * @return
     * @throws
     */
    public Long strlen(String keyT) {
        String key = buildKey(keyT);
        Jedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("strlen", key, pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("strlen",keyT,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.strlen(key);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: sort
     * @description: 将排序结果保存到给定的键上。如果被指定的 key 已存在，那么原有的值将被排序结果覆盖。
     * @param dstkeyT
     * @param sortingParameters
     * @param dstkeyT
     * @return 返回排序结果的元素数量。
     * @throws
     */
    public Long sort(String keyT, SortingParams sortingParameters, String dstkeyT) {
        String key = buildKey(keyT);
        String dstkey = buildKey(dstkeyT);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("sort SortingParams String",keyT,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sort SortingParams String",keyT,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.sort(key, sortingParameters, dstkey);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: sort
     * @description: 将排序结果保存到给定的键上。如果被指定的 key 已存在，那么原有的值将被排序结果覆盖。
     * @param keyT
     * @param dstkeyT
     * @return 返回排序结果的元素数量。
     * @throws
     */
    public Long sort(String keyT, String dstkeyT) {
        String key = buildKey(dstkeyT);
        String dstkey = buildKey(dstkeyT);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("sort String", key,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sort String",keyT,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.sort(key, dstkey);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: smove
     * @description: 将 member 元素从 source 集合移动到 destination 集合。SMOVE 是原子性操作。如果
     *               source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。否则，
     *               member 元素从 source 集合中被移除，并添加到 destination 集合中去。
     * @param srckeyT
     * @param dstkeyT
     * @param member
     * @return 如果 member 元素被成功移除，返回 1 。如果 member 元素不是 source 集合的成员，并且没有任何操作对
     *         destination 集合执行，那么返回 0 。
     * @throws JedisDataException 当 source 或 destination 不是集合类型时，抛出异常。
     */
    public Long smove(String srckeyT, String dstkeyT, String member) {
        String srckey = buildKey(srckeyT);
        String dstkey = buildKey(dstkeyT);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("smove", srckeyT,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("smove",srckeyT,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.smove(srckey, dstkey, member);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public byte[] slowlogReset() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            byte[] ret = null;//jedis.slowlogReset();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public long slowlogLen() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            long ret = jedis.slowlogLen();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public List<byte[]> slowlogGetBinary() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            List<byte[]> ret = jedis.slowlogGetBinary();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public List<byte[]> slowlogGetBinary(long entries) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            List<byte[]> ret = jedis.slowlogGetBinary(entries);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public List<Slowlog> slowlogGet() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            List<Slowlog> ret = jedis.slowlogGet();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public List<Slowlog> slowlogGet(long entries) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            List<Slowlog> ret = jedis.slowlogGet(entries);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public String slaveofNoOne() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.slaveofNoOne();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public String slaveof(String host, int port) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.slaveof(host, port);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    /**
     * 
     * @title: sinterstore
     * @description: 这个命令类似于 SINTER 命令，但它将结果保存到 destination 集合，而不是简单地返回结果集。如果
     *               destination 集合已经存在，则将其覆盖。
     * @param dstkey
     * @param keys
     * @return 结果集中的成员数量。
     * @throws
     */
    public Long sinterstore(String dstkey, String... keys) {
        Jedis jedis = null;
        dstkey = buildKey(dstkey);
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("sinterstore", dstkey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sinterstore",dstkey,logMessage,pool);
        try {
            String[] setArray = new String[keys.length];
            for (int i = 0; i < setArray.length; i++) {
                setArray[i] = buildKey(keys[i]);
            }
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.sinterstore(dstkey, setArray);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: sinter
     * @description: 返回一个集合的全部成员，该集合是所有给定集合的交集。不存在的 key
     *               被视为空集。当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
     * @param keys
     * @return 交集成员的列表。
     * @throws
     */
    public Set<String> sinter(String... keys) {
        Jedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("sinter", getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sinter",getKeyStr(keys),logMessage,pool);
        try {
            String[] setArray = new String[keys.length];
            for (int i = 0; i < setArray.length; i++) {
                setArray[i] = buildKey(keys[i]);
            }
            jedis = (Jedis) pool.getResource();
            Set<String> ret = jedis.sinter(setArray);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: sinterE
     * @description: 返回一个集合的全部成员，该集合是所有给定集合的交集。不存在的 key
     *               被视为空集。当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。
     * @param memberClass
     * @param keys
     * @return 交集成员的列表。
     * @throws
     */
    @SuppressWarnings("unchecked")
    public <E> Set<E> sinterE(Class<E> memberClass, String... keys) {
        BinaryJedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("sinterE", getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sinterE",getKeyStr(keys),logMessage,pool);
        try {
            byte[][] bkeys = new byte[keys.length][];
            for (int i = 0; i < bkeys.length; i++) {
                bkeys[i] = buildKey(keys[i]).getBytes(Protocol.CHARSET);
            }
            jedis = (BinaryJedis) pool.getResource();
            Set<byte[]> values = jedis.sinter(bkeys);
            pool.returnResourceObject(jedis);

            if (values != null && values.size() > 0) {
                Set<E> result = new HashSet<E>(values.size());
                for (byte[] bs : values) {
                    result.add((E) serializer.deserialize(bs));
                }
                t.setStatus(Transaction.SUCCESS);
                return result;
            }
            t.setStatus(Transaction.SUCCESS);
            return null;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public String shutdown() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.shutdown();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public String select(int index) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.select(index);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Long sdiffstore(String dstkeyT, String... keys) {
        Jedis jedis = null;
        String dstkey = buildKey(dstkeyT);
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("sdiffstore", dstkeyT,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sdiffstore",dstkeyT,logMessage,pool);
        try {
            String[] setArray = new String[keys.length];
            for (int i = 0; i < setArray.length; i++) {
                setArray[i] = buildKey(keys[i]);
            }
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.sdiffstore(dstkey, setArray);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: sdiff
     * @description: 返回一个集合的全部成员，该集合是所有给定集合之间的差集。不存在的 key 被视为空集。
     * @param keys
     * @return 差集成员的列表。
     * @throws
     */
    public Set<String> sdiff(String... keys) {
        Jedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("sdiff", getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sdiff",getKeyStr(keys),logMessage,pool);
        try {
            String[] setArray = new String[keys.length];
            for (int i = 0; i < setArray.length; i++) {
                setArray[i] = buildKey(keys[i]);
            }
            jedis = (Jedis) pool.getResource();
            Set<String> ret = jedis.sdiff(setArray);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: sdiffE
     * @description: 返回一个集合的全部成员，该集合是所有给定集合之间的差集。不存在的 key 被视为空集。
     * @param keys
     * @return 差集成员的列表。
     * @throws
     */
    @SuppressWarnings("unchecked")
    public <E> Set<E> sdiffE(Class<E> memberClass, String... keys) {
        BinaryJedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("sdiffE", getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("sdiffE",getKeyStr(keys),logMessage,pool);
        try {
            byte[][] bkey = new byte[keys.length][];
            for (int i = 0; i < bkey.length; i++) {
                bkey[i] = buildKey(keys[i]).getBytes(Protocol.CHARSET);
            }
            jedis = (BinaryJedis) pool.getResource();
            Set<byte[]> set = jedis.sdiff(bkey);
            pool.returnResourceObject(jedis);
            if (set != null && set.size() != 0) {
                Set<E> result = new HashSet<E>();
                for (byte[] bt : set) {
                    result.add((E) serializer.deserialize(bt));
                }
                t.setStatus(Transaction.SUCCESS);
                return result;
            }
            t.setStatus(Transaction.SUCCESS);
            return null;
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public String scriptLoad(String script) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.scriptLoad(script);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public byte[] scriptKill() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            byte[] ret = null;//jedis.scriptKill();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public byte[] scriptFlush() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            byte[] ret = null;//jedis.scriptFlush();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Boolean scriptExists(String sha1) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Boolean ret = jedis.scriptExists(sha1);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public List<Boolean> scriptExists(String[] sha1) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            List<Boolean> ret = jedis.scriptExists(sha1);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public String save() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.save();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    /**
     * 
     * @title: rpoplpush
     * @description: 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端。 将 source 弹出的元素插入到列表
     *               destination ，作为 destination 列表的的头元素。
     * @param srckey
     * @param dstkey
     * @return 被弹出的元素。如果 source 不存在，返回null，并且不执行其他动作。
     * @throws
     */
    public String rpoplpush(String srckey, String dstkey) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("rpoplpush", srckey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("rpoplpush",srckey,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.rpoplpush(srckey, dstkey);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: rpoplpush
     * @description: 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端。 将 source 弹出的元素插入到列表
     *               destination ，作为 destination 列表的的头元素。
     * @param entityClass
     * @param srckey
     * @param dstkey
     * @return 被弹出的元素。如果 source 不存在，返回null，并且不执行其他动作。
     * @throws
     */
    @SuppressWarnings("unchecked")
    public <E> E rpoplpushE(Class<E> entityClass, String srckey, String dstkey) {
        BinaryJedis commands = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("rpoplpushE", srckey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("rpoplpushE",srckey,logMessage,pool);
        try {
            commands = (BinaryJedis) pool.getResource();
            byte[] bsrckey = srckey.getBytes(Protocol.CHARSET);
            byte[] bdstkey = dstkey.getBytes(Protocol.CHARSET);
            byte[] value = commands.rpoplpush(bsrckey, bdstkey);
            pool.returnResourceObject(commands);
            E result = (E) serializer.deserialize(value);
            t.setStatus(Transaction.SUCCESS);
            return result;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (commands != null) {
                pool.returnBrokenResource(commands);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (commands != null) {
                pool.returnResourceObject(commands);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: renamenx
     * @description: 当且仅当 newkey 不存在时，将 key 改名为 newkey 。当 key 不存在时，抛出异常。
     * @param oldkey
     * @param newkey
     * @return 修改成功时，返回 1 。如果 newkey 已经存在，返回 0 。
     * @throws JedisDataException
     */
    public Long renamenx(String oldkey, String newkey) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("renamenx", oldkey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("renamenx",oldkey,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.renamenx(oldkey, newkey);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: rename
     * @description: 将 key 改名为 newkey 。当 key 和 newkey 相同，或者 key 不存在时，抛出异常。当
     *               newkey 已经存在时， RENAME 命令将覆盖旧值。
     * @param oldkey
     * @param newkey
     * @return 改名成功时返回 OK
     * @throws JedisDataException
     */
    public String rename(String oldkey, String newkey) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("rename", oldkey,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("rename",oldkey,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.rename(oldkey, newkey);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: randomKey
     * @description: 从当前数据库中随机返回(不删除)一个 key 。
     * @return 当数据库不为空时，返回一个 key 。当数据库为空时，返回 null 。
     * @throws
     */
    public String randomKey() {
        Jedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        LogMessage logMessage = new LogMessage();
        logMethodStart("randomKey","",logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.randomKey();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
        }
    }

    public byte[] randomBinaryKey() {
        Jedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        LogMessage logMessage = new LogMessage();
        logMethodStart("randomBinaryKey","",logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            byte[] ret = jedis.randomBinaryKey();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
        }
    }

    public String quit() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.quit();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Long publish(String channel, String message) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.publish(channel, message);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public void psubscribe(JedisPubSub jedisPubSub, String[] patterns) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            jedis.psubscribe(jedisPubSub, patterns);
            pool.returnResourceObject(jedis);
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Pipeline pipelined() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Pipeline ret = jedis.pipelined();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public String ping() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.ping();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    /**
     * 
     * @title: persist
     * @description: 移除给定 key 的生存时间，将这个 key 从『可挥发』的(带生存时间 key
     *               )转换成『持久化』的(一个不带生存时间、永不过期的 key )。
     * @param keyT
     * @return 当生存时间移除成功时，返回 1 。如果 key 不存在或 key 没有设置生存时间，返回 0 。
     * @throws
     */
    public Long persist(String keyT) {
        String key = buildKey(keyT);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        LogMessage logMessage = new LogMessage();
        logMethodStart("persist",keyT,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.persist(key);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
        }
    }

    public String objectEncoding(String string) {
        string = buildKey(string);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.objectEncoding(string);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Long objectIdletime(String string) {
        string = buildKey(string);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.objectIdletime(string);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Long objectRefcount(String string) {
        string = buildKey(string);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.objectRefcount(string);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    /**
     * 
     * @title: msetnx
     * @description: 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。即使只有一个给定 key 已存在，
     *               MSETNX 也会拒绝执行所有给定 key 的设置操作。value 为字符串类型。
     * @param keysvalues
     * @return 当所有 key 都成功设置，返回 1 。 如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0 。
     * @throws
     */
    public Long msetnx(String... keysvalues) {
        if (keysvalues == null) {
            return 0l;
        }
        String[] keyvalueA = keysvalues.clone();
        for (int i = 0; i < keysvalues.length; i = i + 2) {
            keyvalueA[i] = buildKey(keyvalueA[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("msetnx String...",getKeyStr(keysvalues),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("msetnx String...",getKeyStr(keysvalues),logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.msetnx(keyvalueA);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: mset
     * @description: 同时设置一个或多个 key-value 对,value为字符串。如果某个给定 key 已经存在，那么 MSET
     *               会用新值覆盖原来的旧值。MSET 是一个原子性(atomic)操作，所有给定 key 都会在同一时间内被设置。
     * @param keysvalues ["key1","value1","key2","value2"]
     * @return 总是返回 OK
     * @throws
     */
    public String mset(String... keysvalues) {
        if (keysvalues == null) {
            return null;
        }
        String[] keyvalueA = keysvalues.clone();
        for (int i = 0; i < keysvalues.length; i = i + 2) {
            keyvalueA[i] = buildKey(keyvalueA[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("mset String...",getKeyStr(keysvalues),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("mset String...",getKeyStr(keysvalues),logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.mset(keyvalueA);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: mset
     * @description: 同时设置一个或多个 key-value 对,value为对象。如果某个给定 key 已经存在，那么 MSET
     *               会用新值覆盖原来的旧值。MSET 是一个原子性(atomic)操作，所有给定 key 都会在同一时间内被设置。
     * @param keysvalues ["key1":"value1","key2":"value2"]
     * @return 总是返回 OK
     * @throws
     */
    public <T> String mset(Map<String, T> keysvalues) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("mset Map", getKeyStr(keysvalues), pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("mset Map",getKeyStr(keysvalues),logMessage,pool);
        try {
            byte[][] bkeysvalue = new byte[keysvalues.size() * 2][];
            int i = 0;
            for (String key : keysvalues.keySet()) {
                bkeysvalue[i] = SafeEncoder.encode(buildKey(key));
                bkeysvalue[i + 1] = serializer.serialize(keysvalues.get(key));
                i += 2;
            }
            jedis = (Jedis) pool.getResource();
            String ret = jedis.mset(bkeysvalue);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: msetnx
     * @description: 同时设置一个或多个 key-value 对,value为对象类型。如果某个给定 key 已经存在，那么 MSET
     *               会用新值覆盖原来的旧值。MSET 是一个原子性(atomic)操作，所有给定 key 都会在同一时间内被设置。
     * @param keysvalues
     * @return 当所有 key 都成功设置，返回 1 。 如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0 。
     * @throws
     */
    public <T> Long msetnx(Map<String, T> keysvalues) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("msetnx Map", getKeyStr(keysvalues),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("msetnx Map",getKeyStr(keysvalues),logMessage,pool);
        try {
            byte[][] bkeysvalue = new byte[keysvalues.size() * 2][];
            int i = 0;
            for (String key : keysvalues.keySet()) {
                bkeysvalue[i] = buildKey(key).getBytes(Protocol.CHARSET);
                bkeysvalue[i + 1] = serializer.serialize(keysvalues.get(key));
                i += 2;
            }
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.msetnx(bkeysvalue);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 
     * @title: move
     * @description: 将当前数据库的 key 移动到给定的数据库 db
     *               当中。如果当前数据库(源数据库)和给定数据库(目标数据库)有相同名字的给定 key ，或者 key
     *               不存在于当前数据库，那么 MOVE 没有任何效果。
     * @param keyT
     * @param dbIndex
     * @return 移动成功返回 1 ，失败则返回 0 。
     * @throws
     */
    public Long move(String keyT, int dbIndex) {
        String key = buildKey(keyT);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.move(key, dbIndex);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public List<String> mget(String... keys) {
        if (keys == null) {
            return null;
        }
        String[] keyA = keys.clone();
        for (int i = 0; i < keyA.length; i++) {
            keyA[i] = buildKey(keyA[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("mget String...",getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("mget String...",getKeyStr(keys),logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            List<String> ret = jedis.mget(keyA);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> mget(Class<T> _class, String... keys) {
        if (keys == null) {
            return null;
        }
        String[] keyA = keys.clone();
        Jedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("mget T", getKeyStr(keys), pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("mget T",getKeyStr(keys),logMessage,pool);
        try {
            byte[][] bkeys = new byte[keys.length][];
            for (int i = 0; i < bkeys.length; ++i) {
                bkeys[i] = SafeEncoder.encode(buildKey(keyA[i]));
            }
            jedis = (Jedis) pool.getResource();
            List<byte[]> values = jedis.mget(bkeys);
            pool.returnResourceObject(jedis);
            if (values != null) {
                List<T> result = new ArrayList<T>();
                for (byte[] value : values) {
                    result.add((T) serializer.deserialize(value));
                }
                t.setStatus(Transaction.SUCCESS);
                return result;
            } else {
                t.setStatus(Transaction.SUCCESS);
                return null;
            }
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /*
     * public Long lastsave() { Jedis jedis = null; try { jedis = (Jedis)
     * getPool().getResource(); return jedis.lastsave(); } catch (Exception e) {
     * log.error("redis error ", e); throw new JedisException(e); } finally {
     * getPool().returnResourceObject(jedis); } }
     */

    /**
     * 
     * @title: keys
     * @description: 查找所有符合给定模式 pattern 的 key 。
     * @param patternT
     * @return 符合给定模式的 key 列表。
     * @throws
     */
    public Set<String> keys(String patternT) {
        String pattern = buildKey(patternT);
        Jedis jedis = null;
        Pool pool = Router.inst.getReadPool();
        Transaction t = catNewTransaction("keys", pattern, pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("keys",patternT,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Set<String> ret = jedis.keys(pattern);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /*
     * public boolean isConnected() { Jedis jedis = null; try { jedis = (Jedis)
     * getPool().getResource(); return jedis.isConnected(); } catch (Exception
     * e) { log.error("redis error ", e); throw new JedisException(e); } finally
     * { getPool().returnResourceObject(jedis); } }
     */
    public Long getDB() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.getDB();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Object evalsha(String script) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Object ret = jedis.evalsha(script);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Object ret = jedis.evalsha(sha1, keys, args);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Object evalsha(String sha1, int keyCount, String... params) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Object ret = jedis.evalsha(sha1, keyCount, params);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Object eval(String script) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Object ret = jedis.eval(script);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Object eval(String sha1, List<String> keys, List<String> args) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Object ret = jedis.eval(sha1, keys, args);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public Object eval(String sha1, int keyCount, String... params) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            Object ret = jedis.eval(sha1, keyCount, params);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    /**
     * 
     * @title: del
     * @description: 删除给定的一个或多个 key 。不存在的 key 会被忽略。
     * @param keys
     * @return 被删除 key 的数量。
     * @throws
     */
    public Long del(String... keys) {
        if (keys == null) {
            return null;
        }
        String[] keyA = keys.clone();
        for (int i = 0; i < keyA.length; i++) {
            keyA[i] = buildKey(keyA[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("del",getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("del",getKeyStr(keys),logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            Long ret = jedis.del(keyA);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public String configSet(String parameter, String value) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.configSet(parameter, value);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public List<String> configGet(String pattern) {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            List<String> ret = jedis.configGet(pattern);
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public String configResetStat() {
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.configResetStat();
            pool.returnResourceObject(jedis);
            return ret;
        }
        catch (JedisException e){
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }
    }

    public String brpoplpush(String sourceT, String destination, int timeout) {
        String source = buildKey(sourceT);
        destination = buildKey(destination);
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("brpoplpush", sourceT,pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("brpoplpush",sourceT,logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            String ret = jedis.brpoplpush(source, destination, timeout);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public List<String> blpop(int timeout, String... keys) {
        if (keys == null) {
            return null;
        }
        String[] keyA = keys.clone();
        for (int i = 0; i < keyA.length; i++) {
            keyA[i] = buildKey(keyA[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("blpop", getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("blpop",getKeyStr(keys),logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            List<String> ret = jedis.blpop(timeout, keyA);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    public List<String> brpop(int timeout, String... keys) {
        if (keys == null) {
            return null;
        }
        String[] keyA = keys.clone();
        for (int i = 0; i < keyA.length; i++) {
            keyA[i] = buildKey(keyA[i]);
        }
        Jedis jedis = null;
        Pool pool = Router.inst.getWritePool();
        Transaction t = catNewTransaction("brpop", getKeyStr(keys),pool);
        LogMessage logMessage = new LogMessage();
        logMethodStart("brpop",getKeyStr(keys),logMessage,pool);
        try {
            jedis = (Jedis) pool.getResource();
            List<String> ret = jedis.brpop(timeout, keyA);
            pool.returnResourceObject(jedis);
            t.setStatus(Transaction.SUCCESS);
            return ret;
        }
        catch (JedisException e){
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis JedisException error ", e);
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
            throw new JedisException(e);
        }
        catch (Exception e) {
            logMethodError(logMessage,e);
            t.setStatus(e);
            log.error("redis error ", e);
            if (jedis != null) {
                pool.returnResourceObject(jedis);
            }
            throw new JedisException(e);
        }finally {
            logMethodEnd(logMessage);
            t.complete();
        }
    }

    /**
     * 从redis取出整数,不存在或者数据不是整数时返回null
     * @param keyT
     * @return
     */
    public Integer getInteger(String keyT) {
        return getInteger(keyT, null);
    }

    /**
     * 从redis取出整数,不存在或者数据不是整数时返回defaultValue
     * @param keyT
     * @param defaultValue
     * @return
     */
    public Integer getInteger(String keyT, Integer defaultValue) {
        String ret = get(keyT);
        if (ret == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(ret);
            }
            catch (Exception e) {
                log.error("从Redis取值时出错，期望Integer, 实际返回是"+ ret, e);
                return defaultValue;
            }
        }
    }
}
