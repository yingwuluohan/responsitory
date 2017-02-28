package com.fang.common.project.redis;

import com.alibaba.fastjson.JSON;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.BinaryClient.LIST_POSITION;

import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;
import redis.clients.util.SafeEncoder;

import java.util.*;

public abstract class AbstractKooJedisClient {

	protected final Log log = LogFactory.getLog(this.getClass());

	protected final Log remoteLogger = LogFactory.getLog("remoteLogger");

	protected final int DEFAUlT_TIMEOUT = 3000;

	protected final int DEFAUlT_DATABASE = 0;

	protected String address;

	protected JedisPoolConfig config;

	protected RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();

	protected String prefix = "";

	protected int version;

	protected String keyPrefix;

	protected int timeOut;

	protected String password;

	protected int database;

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public RedisSerializer<Object> getSerializer() {
		return serializer;
	}

	public void setSerializer(RedisSerializer<Object> serializer) {
		this.serializer = serializer;
	}

	public String getAddress() {
		if (address == null || address.length() == 0) {
			address = PropertiesConfigUtils.getProperty("redis");
		}
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public JedisPoolConfig getConfig() {
		return config;
	}

	public void setConfig(JedisPoolConfig config) {
		this.config = config;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public abstract void init();

	/**
	 *
	 * @title: append
	 * @description: 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。如果
	 *               key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value
	 *               一样。
	 * @param keyT
	 * @param value
	 * @return 追加 value 之后， key 中字符串的长度。
	 * @throws
	 */
	// @Override
	public Long append(final String keyT, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("append", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("append",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.append(key, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		}catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		}catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:append key:" + key, e);
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
	 * @title: decr
	 * @description: 将 key 中储存的数字值减一。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR
	 *               操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * @param keyT
	 * @return 执行 DECR 命令之后 key 的值。
	 * @throws JedisDataException
	 */
	// @Override
	public Long decr(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("decr", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("decr",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.decr(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		}catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:decr key:" + key, e);
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
	 * @title: decrBy
	 * @description: 将 key 所储存的值减去减量 decrement 。如果 key 不存在，那么 key 的值会先被初始化为 0
	 *               ，然后再执行 DECRBY 操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么抛出异常。
	 * @param keyT
	 * @param decrement
	 * @return 减去 decrement 之后， key 的值。
	 * @throws JedisException 如果值包含错误的类型
	 *             ，或字符串类型的值不能表示为数字，那么返回一个错误。
	 */
	// @Override
	public Long decrBy(final String keyT, long decrement) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("sadd", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("decrBy",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.decrBy(key, decrement);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:decrBy key:" + key, e);
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
	 * @title: exists
	 * @description: 检查给定 key 是否存在。
	 * @param keyT
	 * @return 若 key 存在，返回 true ，否则返回 false 。
	 * @throws
	 */
	// @Override
	public Boolean exists(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("exists", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("exists",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Boolean ret = commands.exists(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:exists key:" + key, e);
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
	 * @title: expire
	 * @description: 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * @param keyT
	 * @param expire
	 *            单位：秒
	 * @return 设置成功返回 1 。当 key 不存在，返回 0 。
	 * @throws
	 */
	// @Override
	public Long expire(final String keyT, int expire) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("expire", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("expire",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.expire(key, expire);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:expire key:" + key, e);
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
	 * @title: expireAt
	 * @description: EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。不同在于 EXPIREAT
	 *               命令接受的时间参数是 UNIX 时间戳
	 * @param keyT
	 * @param expireAt
	 *            秒
	 * @return 如果生存时间设置成功，返回 1 。当 key 不存在或没办法设置生存时间，返回 0 。
	 * @throws
	 */
	// @Override
	public Long expireAt(final String keyT, long expireAt) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("expireAt", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("expireAt",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.expireAt(key, expireAt);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:expireAt key:" + key, e);
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
	 * @title: get
	 * @description: 返回 key 所关联的字符串值。如果key不存在则返回null
	 * @param keyT
	 * @return 字符串类型
	 * @throws
	 */
	// @Override
	public String get(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("get", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("get",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.get(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			if(ret == null){
				catCacheMissed("get",pool);
			}
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:get key:" + key, e);
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
	 * @title: getSet
	 * @description: 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
	 * @param keyT
	 * @param value
	 * @return 返回给定 key 的旧值。 当 key 没有旧值时，也即是， key 不存在时，返回 null 。
	 * @throws
	 */
	// @Override
	public String getSet(final String keyT, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("getSet", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("getSet",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.getSet(key, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			if(ret == null){
				catCacheMissed("getSet",pool);
			}
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:getSet key:" + key, e);
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
	 * @title: getbit
	 * @description: 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。当 offset 比字符串值的长度大，或者 key
	 *               不存在时，返回 false 。
	 * @param keyT
	 * @param offset
	 * @return 字符串值指定偏移量上的位(bit)。1:true 2:false
	 * @throws
	 */
	// @Override
	public Boolean getbit(final String keyT, long offset) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("getbit", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("getbit",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Boolean ret = commands.getbit(key, offset);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:getbit key:" + key, e);
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
	 * @title: getrange
	 * @description: 返回 key 中字符串值的子字符串，字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和
	 *               end 在内)。负数偏移量表示从字符串最后开始计数
	 * @param keyT
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return 截取得出的子字符串。
	 * @throws
	 */
	// @Override
	public String getrange(final String keyT, long start, long end) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("getrange", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("getrange",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.getrange(key, start, end);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:getrange key:" + key, e);
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
	 * @title: hdel
	 * @description: 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 * @param keyT
	 * @param fields
	 * @return 被成功移除的域的数量，不包括被忽略的域。
	 * @throws
	 */
	// @Override
	public Long hdel(final String keyT, String... fields) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("hdel", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hdel",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.hdel(key, fields);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hdel key:" + key, e);
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
	 * @title: hexists
	 * @description: 查看哈希表 key 中，给定域 field 是否存在。
	 * @param keyT
	 * @param field
	 * @return 如果哈希表含有给定域，返回 true 。如果哈希表不含有给定域，或 key 不存在，返回 false 。
	 * @throws
	 */
	// @Override
	public Boolean hexists(final String keyT, String field) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hexists", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hexists",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Boolean ret = commands.hexists(key, field);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hexists key:" + key, e);
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
	 * @title: hget
	 * @description: 返回哈希表 key 中给定域 field 的值。
	 * @param keyT
	 * @param field
	 * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 null 。
	 * @throws
	 */
	// @Override
	public String hget(final String keyT, String field) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hget", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hget",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.hget(key, field);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hget key:" + key, e);
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
	 * @title: hgetAll
	 * @description: 返回哈希表 key 中，所有的域和值。
	 * @param keyT
	 * @return 若 key 不存在，返回null。
	 * @throws
	 */
	// @Override
	public Map<String, String> hgetAll(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hgetAll", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hgetAll",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Map<String, String> result = commands.hgetAll(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			if (result != null && result.size() > 0) {
				return result;
			}
			return null;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hgetAll key:" + key, e);
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
	 * @title: hgetAllE
	 * @description: 返回哈希表 key 中，所有的域和值。
	 * @param keyT
	 * @return 若 key 不存在，返回null。
	 * @throws
	 */
	// @Override
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> hgetAllE(Class<T> entityClass, final String keyT) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hgetAllE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hgetAllE",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			Map<byte[], byte[]> map = commands.hgetAll(key.getBytes(Protocol.CHARSET));
			pool.returnResourceObject(commands);
			if (map != null && map.size() != 0) {
				Map<String, T> result = new HashMap<String, T>();
				for (byte[] bkey : map.keySet()) {
					result.put(new String(bkey),
							(T) serializer.deserialize(map.get(bkey)));
				}
				t.setStatus(Transaction.SUCCESS);
				return result;
			}
			t.setStatus(Transaction.SUCCESS);
			return null;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hgetAllE key:" + key, e);
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
	 * @title: hincrBy
	 * @description: 为哈希表 key 中的域 field 的值加上增量 increment
	 *               。增量也可以为负数，相当于对给定域进行减法操作。如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY
	 *               命令。如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 * @param keyT
	 * @param field
	 * @param increment
	 * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值。
	 * @throws JedisDataException
	 *             对一个储存字符串值的域 field 执行 HINCRBY 命令将抛出异常。
	 */
	// @Override
	public Long hincrBy(final String keyT, String field, long increment) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("hincrBy", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hincrBy",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.hincrBy(key, field, increment);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hincrBy key:" + key, e);
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
	 * @title: hkeys
	 * @description: 返回哈希表 key 中的所有域。
	 * @param keyT
	 * @return 当 key 不存在时，返回null。
	 * @throws
	 */
	// @Override
	public Set<String> hkeys(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hkeys", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hkeys",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> result = commands.hkeys(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			if (result != null && result.size() > 0) {
				return result;
			}
			return null;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hkeys key:" + key, e);
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
	 * @title: hlen
	 * @description: 返回哈希表 key 中域的数量。
	 * @param keyT
	 * @return 哈希表 key 中域的数量。
	 * @throws
	 */
	// @Override
	public Long hlen(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hlen", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hlen",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.hlen(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hlen key:" + key, e);
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
	 * @title: hmget
	 * @description: 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回全null 值的list。
	 * @param keyT
	 * @param fields
	 * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
	 * @throws JedisDataException
	 *             当key不是哈希表时抛出异常
	 */
	// @Override
	public List<String> hmget(final String keyT, String... fields) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hmget", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hmget",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			List<String> ret = commands.hmget(key, fields);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hmget key:" + key, e);
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
	 * @title: hmgetE
	 * @description: 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回全null 值的list。
	 * @param _class
	 * @param keyT
	 * @param fields
	 * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
	 * @throws JedisDataException
	 *             当key不是哈希表时抛出异常
	 */
	// @Override
	@SuppressWarnings("unchecked")
	public <V> List<V> hmgetE(Class<V> _class, final String keyT,
			String... fields) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hmgetE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hmgetE",keyT,logMessage,pool);
		try {
			byte[][] bkeys = new byte[fields.length][];
			for (int i = 0; i < bkeys.length; ++i) {
				bkeys[i] = SafeEncoder.encode(fields[i]);
			}
			commands = (BinaryJedisCommands) pool.getResource();
			List<byte[]> values = commands.hmget(key.getBytes(Protocol.CHARSET), bkeys);
			pool.returnResourceObject(commands);
			if (values != null) {
				List<V> result = new ArrayList<V>();
				for (byte[] value : values) {
					result.add((V) serializer.deserialize(value));
				}
				t.setStatus(Transaction.SUCCESS);
				return result;
			} else {
				t.setStatus(Transaction.SUCCESS);
				return null;
			}
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hmgetE key:" + key, e);
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
	 * @title: hmset
	 * @description: 同时将多个 field-value (域-值)对设置到哈希表 key 中。此命令会覆盖哈希表中已存在的域。如果 key
	 *               不存在，一个空哈希表被创建并执行 HMSET 操作。
	 * @param keyT
	 * @param paramMap
	 * @return 如果命令执行成功，返回 OK 。
	 * @throws JedisDataException
	 *             当 key 不是哈希表(hash)类型时，抛出异常。
	 */
	// @Override
	public String hmset(final String keyT, Map<String, String> paramMap) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("hmset Map", key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hmset Map",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.hmset(key, paramMap);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage, e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage, e);
			t.setStatus(e);
			log.error("redis error method:hmset key:" + key, e);
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
	 * @title: hmset
	 * @description: 同时将多个 field-value (域-值)对设置到哈希表 key 中。此命令会覆盖哈希表中已存在的域。如果 key
	 *               不存在，一个空哈希表被创建并执行 HMSET 操作。
	 * @param keyT
	 * @param paramMap
	 * @return 如果命令执行成功，返回 OK 。
	 * @throws JedisDataException
	 *             当 key 不是哈希表(hash)类型时，抛出异常。
	 */
	// @Override
	public <V> String hmsetE(final String keyT, Map<String, V> paramMap) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("hmsetE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hmsetE",keyT,logMessage,pool);
		try {
			Map<byte[], byte[]> bhash = new HashMap<byte[], byte[]>(
					paramMap.size());
			for (Map.Entry<String, V> entry : paramMap.entrySet()) {
				bhash.put(SafeEncoder.encode(entry.getKey()),
						serializer.serialize(entry.getValue()));
			}
			commands = (BinaryJedisCommands) pool.getResource();
			String ret = commands.hmset(SafeEncoder.encode(key), bhash);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hmsetE key:" + key, e);
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
	 * @title: hset
	 * @description: 将哈希表 key 中的域 field 的值设为 value 。如果 key 不存在，一个新的哈希表被创建并进行
	 *               HSET 操作。如果域 field 已经存在于哈希表中，旧值将被覆盖。
	 * @param keyT
	 * @param field
	 * @param value
	 * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回
	 *         0 。
	 * @throws
	 */
	// @Override
	public Long hset(final String keyT, String field, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("hset String", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hset String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.hset(key, field, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hset key:" + key, e);
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
	 * @title: hsetnx
	 * @description: 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。若域 field
	 *               已经存在，该操作无效。如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
	 * @param keyT
	 * @param field
	 * @param value
	 * @return 设置成功，返回 1 。如果给定域已经存在且没有操作被执行，返回 0 。
	 * @throws
	 */
	// @Override
	public Long hsetnx(final String keyT, String field, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("hsetnx", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hsetnx",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.hsetnx(key, field, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hsetnx key:" + key, e);
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
	 * @title: hsetnxE
	 * @description: 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。若域 field
	 *               已经存在，该操作无效。如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
	 * @param keyT
	 * @param field
	 * @param value
	 * @return 设置成功，返回 1 。如果给定域已经存在且没有操作被执行，返回 0 。
	 * @throws
	 */
	// @Override
	public <V> Long hsetnxE(final String keyT, String field, V value) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("hsetnxE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hsetnxE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] bhkey = field.getBytes(Protocol.CHARSET);
			byte[] bvalue = serializer.serialize(value);
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.hsetnx(bkey, bhkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hsetnxE key:" + key, e);
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
	 * @title: hvals
	 * @description: 返回哈希表 key 中所有域的值。
	 * @param keyT
	 * @return 哈希表 key 中所有域的值。当 key 不存在时，返回一个空表。
	 * @throws
	 */
	// @Override
	public List<String> hvals(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hvals", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hvals",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			List<String> ret = commands.hvals(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hvals key:" + key, e);
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
	 * @title: incr
	 * @description: 将 key 中储存的数字值增一。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR
	 *               操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么抛出异常。
	 * @param keyT
	 * @return 执行 INCR 命令之后 key 的值。
	 * @throws JedisDataException
	 */
	// @Override
	public Long incr(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("incr", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("incr",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.incr(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:incr key:" + key, e);
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
	 * @title: incrBy
	 * @description: 将 key 所储存的值加上增量 increment 。如果 key 不存在，那么 key 的值会先被初始化为 0
	 *               ，然后再执行 INCRBY 命令。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么抛出异常。
	 * @param keyT
	 * @param increment
	 *            增加数
	 * @return 加上 increment 之后， key 的值。
	 * @throws JedisDataException
	 */
	// @Override
	public Long incrBy(final String keyT, long increment) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("incrBy", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("incrBy",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.incrBy(key, increment);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:incrBy key:" + key, e);
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
	 * @title: lindex
	 * @description: 返回列表 key 中，下标为 index 的元素。
	 * @param keyT
	 * @param index
	 * @return 列表中下标为 index 的元素。如果 index 参数的值不在列表的区间范围内(out of range)，返回null 。
	 * @throws JedisDataException
	 *             如果 key 不是列表类型，抛出异常。
	 */
	public String lindex(final String keyT, long index) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("lindex", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lindex",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.lindex(key, index);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lindex key:" + key, e);
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
	 * @title: linsert
	 * @description: 将值 value 插入到列表 key 当中，位于值 pivot 之前或之后。当 pivot 不存在于列表 key
	 *               时，不执行任何操作。当 key 不存在时， key 被视为空列表，不执行任何操作。
	 * @param keyT
	 * @param paramLIST_POSITION
	 * @param pivot
	 * @param value
	 * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。如果没有找到 pivot ，返回 -1 。如果 key 不存在或为空列表，返回
	 *         0 。
	 * @throws JedisDataException
	 *             如果 key 不是列表类型，抛出异常。
	 */
	public Long linsert(final String keyT, LIST_POSITION paramLIST_POSITION,
			String pivot, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("linsert", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("linsert",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.linsert(key, paramLIST_POSITION, pivot, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:linsert key:" + key, e);
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
	 * @title: llen
	 * @description: 返回列表 key 的长度。如果 key 不存在，则 key 被解释为一个空列表，返回 0 。
	 * @param keyT
	 * @return 列表 key 的长度。
	 * @throws JedisDataException
	 *             如果 key 不是列表类型，抛出异常。
	 */
	public Long llen(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("llen", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("llen",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.llen(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:llen key:" + key, e);
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
	 * @title: lpop
	 * @description: 移除并返回列表 key 的头元素。
	 * @param keyT
	 * @return 列表的头元素。当 key 不存在时，返回 null 。
	 * @throws
	 */
	public String lpop(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lpop", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lpop",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.lpop(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lpop key:" + key, e);
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
	 * @title: lpush
	 * @description: 将一个或多个值 value 插入到列表 key 的表头,如果有多个 value 值，那么各个 value
	 *               值按从左到右的顺序依次插入到表头.如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
	 * @param keyT
	 * @param paramArrayOfString
	 * @return 执行 LPUSH 命令后，列表的长度。
	 * @throws JedisDataException
	 *             当 key 存在但不是列表类型时，抛出异常。
	 */
	// @Override
	public Long lpush(final String keyT, String... paramArrayOfString) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lpush String...",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lpush String...",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.lpush(key, paramArrayOfString);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lpush key:" + key, e);
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
	 * @title: lpush
	 * @description: 将一个或多个值 value 插入到列表 key 的表头,如果有多个 value 值，那么各个 value
	 *               值按从左到右的顺序依次插入到表头.如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
	 * @param keyT
	 * @param paramArrayOfString
	 * @return 执行 LPUSH 命令后，列表的长度。
	 * @throws JedisDataException
	 *             当 key 存在但不是列表类型时，抛出异常。
	 */
	public Long lpush(final String keyT, List<String> paramArrayOfString) {
		String key = buildKey(keyT);
		if (paramArrayOfString == null) {
			return 0l;
		}
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lpush List", key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lpush List",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.lpush(key, paramArrayOfString
					.toArray(new String[paramArrayOfString.size()]));
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lpush list key:" + key, e);
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
	 * @title: rpush
	 * @description: 将一个或多个值 value 插入到列表 key 的表尾,如果有多个 value 值，那么各个 value
	 *               值按从左到右的顺序依次插入到表尾.如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
	 * @param keyT
	 * @param paramArrayOfString
	 * @return 执行 RPUSH 命令后，列表的长度。
	 * @throws JedisDataException
	 *             当 key 存在但不是列表类型时，抛出异常。
	 */
	public Long rpush(final String keyT, List<String> paramArrayOfString) {
		String key = buildKey(keyT);
		if (paramArrayOfString == null) {
			return 0l;
		}
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("rpush List", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("rpush List",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.rpush(key, paramArrayOfString
					.toArray(new String[paramArrayOfString.size()]));
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:rpush key:" + key, e);
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
	 * @title: lpushx
	 * @description: 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。和 LPUSH 命令相反，当
	 *               key 不存在时， LPUSHX 命令什么也不做。
	 * @param keyT
	 * @param value
	 * @return LPUSHX 命令执行之后，表的长度。
	 * @throws
	 */
	// @Override
	public Long lpushx(final String keyT, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lpushx", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lpushx",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.lpushx(key, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lpushx key:" + key, e);
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
	 *
	 * @title: lrange
	 * @description: 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。下标(index)参数 start
	 *               和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1
	 *               表示列表的第二个元素，以此类推。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2
	 *               表示列表的倒数第二个元素，以此类推。 超出范围的下标值不会引起错误。 如果 start 下标比列表的最大下标 end
	 *               ( LLEN list 减去 1 )还要大，或者 start > stop ， LRANGE 返回一个空列表。 如果
	 *               stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
	 * @param keyT
	 * @param start
	 * @param stop
	 * @return 个列表，包含指定区间内的元素。
	 * @throws JedisDataException
	 *             当 key 存在但不是列表类型时，抛出异常。
	 */
	// @Override
	public List<String> lrange(final String keyT, long start, long stop) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("lrange", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lrange",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			List<String> ret = commands.lrange(key, start, stop);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lrange key:" + key, e);
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
	 * @title: lrem
	 * @description: 根据参数 count 的值，移除列表中与参数 value 相等的元素。count 的值可以是以下几种： count >
	 *               0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。 count < 0 :
	 *               从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。 count = 0 :
	 *               移除表中所有与 value 相等的值
	 * @param keyT
	 * @param count
	 * @param value
	 * @return 被移除元素的数量。为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0
	 *         。
	 * @throws
	 */
	public Long lrem(final String keyT, long count, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lrem", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lrem",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.lrem(key, count, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lrem key:" + key, e);
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
	 * @title: lset
	 * @description: 将列表 key 下标为 index 的元素的值设置为 value 。
	 * @param keyT
	 * @param index
	 * @param value
	 * @return 操作成功返回 ok
	 * @throws JedisDataException
	 *             当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，抛出异常。
	 */
	public String lset(final String keyT, long index, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lset", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lset",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.lset(key, index, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lset key:" + key, e);
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
	 * @title: ltrim
	 * @description: 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。如果
	 *               start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，或者 start > stop
	 *               ， LTRIM 返回一个空列表(因为 LTRIM 已经将整个列表清空)。如果 stop 下标比 end
	 *               下标还要大，Redis将 stop 的值设置为 end 。
	 * @param keyT
	 * @param start
	 * @param stop
	 * @return 命令执行成功时，返回 ok 。
	 * @throws JedisDataException
	 *             当 key 不是列表类型时，抛出异常。
	 */
	public String ltrim(final String keyT, long start, long stop) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("ltrim", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("ltrim",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.ltrim(key, start, stop);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:ltrim key:" + key, e);
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
	 * @title: rpop
	 * @description: 移除并返回列表 key 的尾元素。
	 * @param keyT
	 * @return 列表的尾元素。当 key 不存在时，返回 null 。
	 * @throws
	 */
	public String rpop(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("rpop", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("rpop",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.rpop(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:rpop key:" + key, e);
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
	 * @title: rpush
	 * @description:
	 * @param keyT
	 * @param paramArrayOfString
	 * @return
	 * @throws
	 */
	public Long rpush(final String keyT, String... paramArrayOfString) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("rpush String...",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("rpush String...",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.rpush(key, paramArrayOfString);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:rpush key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long rpushx(String keyT, String paramString2) {
		String key = buildKey(keyT);

		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("rpushx String",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("rpushx String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.rpushx(key, paramString2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:rpushx key:" + key, e);
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
	 * @title: sadd
	 * @description: 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。假如 key
	 *               不存在，则创建一个只包含 member 元素作成员的集合。
	 * @param keyT
	 * @param paramArrayOfString
	 * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 * @throws JedisDataException
	 *             当 key 不是集合类型时，抛出异常。
	 */
	public Long sadd(final String keyT, String... paramArrayOfString) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("sadd",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("sadd",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.sadd(key, paramArrayOfString);
			pool.returnResourceObject(commands);

			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:sadd key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Transaction catNewTransaction(String nameForCat,String key,Pool pool){
		String serverIpAndPort = Router.inst.getAddressAndPortByPool(pool);
		Transaction t = Cat.newTransaction("Cache.redis-" + serverIpAndPort, nameForCat);
		t.addData("key", key);

		Cat.logEvent("Cache.redis.server", serverIpAndPort);

		return t;
	}

	public void catCacheMissed(String nameForCat,Pool pool){
		String serverIpAndPort = Router.inst.getAddressAndPortByPool(pool);
		Cat.logEvent("Cache.redis-" + serverIpAndPort, nameForCat + ":missed");
	}

	/**
	 *
	 * @title: scard
	 * @description: 返回集合 key 的基数(集合中元素的数量)。
	 * @param keyT
	 * @return 集合的基数。当 key 不存在时，返回 0 。
	 * @throws
	 */
	public Long scard(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("scard", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("scard",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.scard(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:scard key:" + key, e);
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
	 * @title: set
	 * @description: 设置字符串类型的value
	 * @param keyT
	 * @param value
	 * @return 总是返回"OK",因为 SET 不可能失败。
	 * @throws
	 */
	// @Override
	public String set(final String keyT, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("set String", key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("set String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.set(key, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:set key:" + key, e);
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
	 * @title: setnx
	 * @description: 设置实体类型value，当且仅当 key 不存在。若给定的 key 已经存在，则 SETNX 不做任何动作。
	 * @param keyT
	 * @param entity
	 * @return 设置成功，返回 1 ;设置失败，返回 0 。
	 * @throws
	 */
	// @Override
	public <T> Long setnxE(final String keyT, T entity) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("setnxE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("setnxE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] bvalue = serializer.serialize(entity);
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.setnx(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:setnxE key:" + key, e);
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
	 * @title: setbit
	 * @description: 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。位的设置或清除取决于 value 参数，可以是 0
	 *               也可以是 1 。当 key 不存在时，自动生成一个新的字符串值。字符串会进行伸展(grown)以确保它可以将
	 *               value 保存在指定的偏移量上。当字符串值进行伸展时，空白位置以 0 填充。
	 * @param keyT
	 * @param offset
	 * @param value
	 *            true:1 false:0
	 * @return 指定偏移量原来储存的位。
	 * @throws
	 */
	// @Override
	public Boolean setbit(final String keyT, long offset, boolean value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("setbit", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("setbit",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Boolean ret = commands.setbit(key, offset, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:setbit key:" + key, e);
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
	 * @title: setex
	 * @description: 设置字符串类型value，并将 key 的生存时间设为 seconds (以秒为单位) 。如果 key 已经存在，
	 *               SETEX 命令将覆写旧值。
	 * @param keyT
	 * @param expire
	 *            单位：秒
	 * @param value
	 * @return 设置成功时返回 OK 。
	 * @throws
	 */
	// @Override
	public String setex(final String keyT, int expire, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("setex String",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("setex String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.setex(key, expire, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:setex key:" + key, e);
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
	 * @title: setnx
	 * @description: 设置字符串类型value，当且仅当 key 不存在。若给定的 key 已经存在，则 SETNX 不做任何动作。
	 * @param keyT
	 * @param value
	 * @return 设置成功，返回 1 ;设置失败，返回 0 。
	 * @throws
	 */
	// @Override
	public Long setnx(final String keyT, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("setnx", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("setnx",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.setnx(key, value);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:setnx key:" + key, e);
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
	 * @title: setrange
	 * @description: 用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始。不存在的
	 *               key 当作空白字符串处理。
	 * @param keyT
	 * @param offset
	 *            最大偏移量：2^29-1(536870911)
	 * @param value
	 * @return 被 SETRANGE 修改之后，字符串的长度。
	 * @throws
	 */
	// @Override
	public Long setrange(final String keyT, long offset, String value) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		LogMessage logMessage = new LogMessage();
		logMethodStart("setrange",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.setrange(key, offset, value);
			pool.returnResourceObject(commands);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			log.error("redis error method:setrange key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
		}
	}

	/**
	 *
	 * @title: sismember
	 * @description: 判断 member 元素是否集合 key 的成员。
	 * @param keyT
	 * @param member
	 * @return 如果 member 元素是集合的成员，返回 true 。如果 member 元素不是集合的成员，或 key 不存在，返回
	 *         false 。
	 * @throws
	 */
	public Boolean sismember(final String keyT, String member) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("sismember", key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("sismember",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Boolean ret = commands.sismember(key, member);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:sismember key:" + key, e);
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
	 * @title: sismemberE
	 * @description: 判断 member 元素是否集合 key 的成员。
	 * @param keyT
	 * @param member
	 * @return 如果 member 元素是集合的成员，返回 true 。如果 member 元素不是集合的成员，或 key 不存在，返回
	 *         false 。
	 * @throws
	 */
	public <E> Boolean sismemberE(final String keyT, E member) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("sismemberE", key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("sismemberE",keyT,logMessage,pool);
		try {
			byte[] bmem = serializer.serialize(member);
			commands = (BinaryJedisCommands) pool.getResource();
			Boolean ret = commands.sismember(key.getBytes(Protocol.CHARSET), bmem);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;

		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:sismemberE key:" + key, e);
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
	 * @title: smembers
	 * @description: 返回集合 key 中的所有成员。
	 * @param keyT
	 * @return 集合中的所有成员。
	 * @throws
	 */
	public Set<String> smembers(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("smembers", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("smembers",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.smembers(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:smembers key:" + key, e);
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
	 * @title: sort
	 * @description: 返回或保存给定列表、集合、有序集合 key
	 *               中经过排序的元素。排序默认以数字作为对象，值被解释为双精度浮点数，然后进行比较。只应用于字符串类型
	 * @param keyT
	 * @return
	 * @throws
	 */
	// @Override
	public List<String> sort(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("sort", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("sort",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			List<String> ret = commands.sort(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:sort key:" + key, e);
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
	 * @title: sort
	 * @description: 返回或保存给定列表、集合、有序集合 key
	 *               中经过排序的元素。排序默认以数字作为对象，值被解释为双精度浮点数，然后进行比较。只应用于字符串类型
	 * @param keyT
	 * @param paramSortingParams
	 *            使用KooSortingParams
	 * @return
	 * @throws
	 */
	// @Override
	public List<String> sort(final String keyT, SortingParams paramSortingParams) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("sort SortingParams",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("sort SortingParams",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			List<String> ret = commands.sort(key, paramSortingParams);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:sort sprams key:" + key, e);
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
	 * @title: spop
	 * @description: 移除并返回集合中的一个随机元素。
	 * @param keyT
	 * @return 被移除的随机元素。当 key 不存在或 key 是空集时，返回 null 。
	 * @throws
	 */
	public String spop(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("spop", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("spop",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.spop(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:spop key:" + key, e);
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
	 * @title: srandmember
	 * @description: 返回集合中的一个随机元素。
	 * @param keyT
	 * @return 如果集合为空，返回null 。
	 * @throws
	 */
	public String srandmember(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("srandmember", key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("srandmember",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.srandmember(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:srandmember key:" + key, e);
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
	 * @title: srem 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 * @description:
	 * @param keyT
	 * @param paramArrayOfString
	 * @return 被成功移除的元素的数量，不包括被忽略的元素。
	 * @throws JedisDataException
	 *             当 key 不是集合类型，抛出异常。
	 */
	// @Override
	public Long srem(final String keyT, String... paramArrayOfString) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("srem", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("srem",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.srem(key, paramArrayOfString);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:srem key:" + key, e);
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
	 * @title: sremE
	 * @description: 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 * @param keyT
	 * @param members
	 * @return 被成功移除的元素的数量，不包括被忽略的元素。
	 * @throws JedisDataException
	 *             当 key 不是集合类型，抛出异常。
	 */
	public <E> Long sremE(final String keyT, E... members) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("sremE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("sremE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[][] bvalue = new byte[members.length][];
			for (int i = 0; i < bvalue.length; i++) {
				bvalue[i] = serializer.serialize(members[i]);
			}
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.srem(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:sremE key:" + key, e);
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
	 * @title: substr
	 * @description: 与getrange 相同， 在 <= 2.0 的版本里使用
	 * @param keyT
	 * @param paramInt1
	 * @param paramInt2
	 * @return
	 * @throws
	 */
	public String substr(String keyT, int paramInt1, int paramInt2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		LogMessage logMessage = new LogMessage();
		logMethodStart("substr",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.substr(key, paramInt1, paramInt2);
			pool.returnResourceObject(commands);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			log.error("redis error method:substr key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
		}
	}

	/**
	 *
	 * @title: ttl
	 * @description: 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
	 * @param keyT
	 * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。否则，以秒为单位，返回 key
	 *         的剩余生存时间。
	 * @throws
	 */
	public Long ttl(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("ttl", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("ttl",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.ttl(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:ttl key:" + key, e);
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
	 * @title: type
	 * @description: 返回 key 所储存的值的类型。
	 * @param keyT
	 * @return none (key不存在) string (字符串)list (列表) set (集合) zset (有序集) hash
	 *         (哈希表)
	 * @throws
	 */
	public String type(final String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("type", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("type",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			String ret = commands.type(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
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

	public Long zadd(String keyT, Map<Double, String> paramMap) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("zadd Map", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zadd Map",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = null;//commands.zadd(key, paramMap);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zadd map key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long zadd(String keyT, double paramDouble,
			String paramString2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("zadd", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zadd",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.zadd(key, paramDouble, paramString2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zadd key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long zcard(String keyT) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zcard", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zcard",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.zcard(key);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zcard key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long zcount(String keyT, double paramDouble1,
			double paramDouble2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zcount double",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zcount double",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.zcount(key, paramDouble1, paramDouble2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zcount double key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long zcount(String keyT, String paramString2,
			String paramString3) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zcount String",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zcount String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands
					.zcount(key, paramString2, paramString3);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zcount key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Double zincrby(String keyT, double paramDouble,
			String paramString2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("zincrby", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zincrby",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Double ret = commands.zincrby(key, paramDouble,
					paramString2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zincrby key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrange(String keyT, long paramLong1,
			long paramLong2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrange", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrange",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrange(key, paramLong1,
					paramLong2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrange key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrangeByScore(String keyT, double paramDouble1,
			double paramDouble2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrangeByScore double",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrangeByScore double",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrangeByScore(key, paramDouble1,
					paramDouble2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrangeByScore double key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrangeByScore(String keyT, String paramString2,
			String paramString3) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrangeByScore String",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrangeByScore String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrangeByScore(key,
					paramString2, paramString3);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrangeByScore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrangeByScore(String keyT, double paramDouble1,
			double paramDouble2, int paramInt1, int paramInt2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrangeByScore double offset",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrangeByScore double offset",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrangeByScore(key, paramDouble1,
					paramDouble2, paramInt1, paramInt2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrangeByScore duble int key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrangeByScore(String keyT, String paramString2,
			String paramString3, int paramInt1, int paramInt2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrangeByScore String offset",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrangeByScore String offset",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrangeByScore(key,
					paramString2, paramString3, paramInt1, paramInt2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrangeByScore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<Tuple> zrangeByScoreWithScores(String keyT,
											  double paramDouble1, double paramDouble2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrangeByScoreWithScores double",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrangeByScoreWithScores double",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrangeByScoreWithScores(key,
					paramDouble1, paramDouble2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrangeByScoreWithScores key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<Tuple> zrangeByScoreWithScores(String keyT,
											  String paramString2, String paramString3) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrangeByScoreWithScores String",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrangeByScoreWithScores String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrangeByScoreWithScores(key,
					paramString2, paramString3);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrangeByScoreWithScores key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<Tuple> zrangeByScoreWithScores(String keyT,
											  double paramDouble1, double paramDouble2, int paramInt1,
											  int paramInt2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrangeByScoreWithScores double offset",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrangeByScoreWithScores double offset",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrangeByScoreWithScores(key,
					paramDouble1, paramDouble2, paramInt1, paramInt2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrangeByScoreWithScores key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<Tuple> zrangeByScoreWithScores(String keyT,
											  String paramString2, String paramString3, int paramInt1,
											  int paramInt2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrangeByScoreWithScores String offset",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrangeByScoreWithScores String offset",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrangeByScoreWithScores(key,
					paramString2, paramString3, paramInt1, paramInt2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrangeByScoreWithScores key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<Tuple> zrangeWithScores(String keyT, long paramLong1,
									   long paramLong2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrangeWithScores",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrangeWithScores",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrangeWithScores(key, paramLong1,
					paramLong2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrangeWithScores key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}

	}

	public Long zrank(String keyT, String paramString2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrank", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrank",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.zrank(key, paramString2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrank key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long zrem(String keyT, String... paramArrayOfString) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("zrem", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrem",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.zrem(key, paramArrayOfString);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrem key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long zremrangeByRank(String keyT, long paramLong1,
			long paramLong2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("zremrangeByRank",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zremrangeByRank",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.zremrangeByRank(key, paramLong1,
					paramLong2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zremrangeByRank key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long zremrangeByScore(String keyT, double paramDouble1,
			double paramDouble2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("zremrangeByScore double",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zremrangeByScore double",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.zremrangeByScore(key, paramDouble1,
					paramDouble2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zremrangeByScore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long zremrangeByScore(String keyT, String paramString2,
			String paramString3) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("zremrangeByScore String",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zremrangeByScore String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.zremrangeByScore(key, paramString2,
					paramString3);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zremrangeByScore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrevrange(String keyT, long paramLong1,
			long paramLong2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrange", key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrange",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrevrange(key, paramLong1,
					paramLong2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrevrange key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrevrangeByScore(String keyT,
			double paramDouble1, double paramDouble2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrangeByScore double",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrangeByScore double",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrevrangeByScore(key,
					paramDouble1, paramDouble2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrevrangeByScore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrevrangeByScore(String keyT,
			String paramString2, String paramString3) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrangeByScore String",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrangeByScore String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrevrangeByScore(key,
					paramString2, paramString3);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error zrerangeByScore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrevrangeByScore(String keyT,
			double paramDouble1, double paramDouble2, int paramInt1,
			int paramInt2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrangeByScore double offset",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrangeByScore double offset",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrangeByScore(key, paramDouble1,
					paramDouble2, paramInt1, paramInt2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrevrangeByScore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<String> zrevrangeByScore(String keyT,
			String paramString2, String paramString3, int paramInt1,
			int paramInt2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrangeByScore String offset",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrangeByScore String offset",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<String> ret = commands.zrangeByScore(key,
					paramString2, paramString3, paramInt1, paramInt2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrevrangeByScore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String keyT,
												 double paramDouble1, double paramDouble2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrangeByScoreWithScores double",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrangeByScoreWithScores double",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrangeByScoreWithScores(key,
					paramDouble1, paramDouble2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error zrevrangeByScoreWithScores key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String keyT,
												 String paramString2, String paramString3) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrangeByScoreWithScores String",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrangeByScoreWithScores String",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrangeByScoreWithScores(key,
					paramString2, paramString3);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
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

	public Set<Tuple> zrevrangeByScoreWithScores(String keyT,
												 double paramDouble1, double paramDouble2, int paramInt1,
												 int paramInt2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrangeByScoreWithScores double offset",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrangeByScoreWithScores double offset",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrangeByScoreWithScores(key,
					paramDouble1, paramDouble2, paramInt1, paramInt2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrevrangeByScoreWithScores key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String keyT,
												 String paramString2, String paramString3, int paramInt1,
												 int paramInt2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrangeByScoreWithScores String offset",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrangeByScoreWithScores String offset",keyT,logMessage,pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrevrangeByScoreWithScores(key,
					paramString2, paramString3, paramInt1, paramInt2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrevrangeByScoreWithScores key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Set<Tuple> zrevrangeWithScores(String keyT, long paramLong1,
										  long paramLong2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrangeWithScores",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrangeWithScores", keyT, logMessage, pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Set<Tuple> ret = commands.zrevrangeWithScores(key,
					paramLong1, paramLong2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrevrangeWithScore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Long zrevrank(String keyT, String paramString2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zrevrank", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zrevrank", keyT, logMessage, pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Long ret = commands.zrevrank(key, paramString2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zrevrank key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public Double zscore(String keyT, String paramString2) {
		String key = buildKey(keyT);
		JedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("zscore", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("zscore", keyT, logMessage, pool);
		try {
			commands = (JedisCommands) pool.getResource();
			Double ret = commands.zscore(key, paramString2);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:zscore key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	public void destroy() {
		Router.inst.destroy();
	}

	@SuppressWarnings("unchecked")
	public <T> T hgetE(Class<T> entityClass, final String keyT, String field) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hgetE",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hgetE", keyT, logMessage, pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] bhkey = field.getBytes(Protocol.CHARSET);
			byte[] value = commands.hget(bkey, bhkey);
			pool.returnResourceObject(commands);
			T result = (T) serializer.deserialize(value);
			t.setStatus(Transaction.SUCCESS);
			return result;
		} catch (JedisException e){
			logMethodError(logMessage, e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hgetE key:" + key, e);
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
	 * @title: get
	 * @description: 返回 key 所关联的实体对象。
	 * @param keyT
	 * @param entityClass
	 *            实体类型
	 * @return entityClass类型的对象
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String keyT, Class<T> entityClass) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("get T", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("get T", keyT, logMessage, pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] value = commands.get(bkey);
			pool.returnResourceObject(commands);
			T result = (T) serializer.deserialize(value);
			t.setStatus(Transaction.SUCCESS);
			if(result == null){
				catCacheMissed("get T",pool);
			}
			return result;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:get key:" + key, e);
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
	 * @title: hset
	 * @description: 将哈希表 key 中的域 field 的值设为 value 。如果 key 不存在，一个新的哈希表被创建并进行
	 *               HSET 操作。如果域 field 已经存在于哈希表中，旧值将被覆盖。
	 * @param keyT
	 * @param field
	 * @param value
	 * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回
	 *         0 。
	 * @throws
	 */
	public <T> Long hset(final String keyT, String field, T value) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("hset T", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hset T",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] bhkey = field.getBytes(Protocol.CHARSET);
			byte[] bvalue = serializer.serialize(value);
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.hset(bkey, bhkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hset key:" + key, e);
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
	 * @title: hvals
	 * @description: 返回哈希表 key 中所有域的值。
	 * @param entityClass
	 * @param keyT
	 * @return 哈希表 key 中所有域的值。当 key 不存在时，返回一个空表。
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> hvals(Class<T> entityClass, final String keyT) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("hvals T", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("hvals T",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			Collection<byte[]> values = commands.hvals(bkey);
			pool.returnResourceObject(commands);
			if (values != null) {
				List<T> result = new ArrayList<T>();
				for (byte[] bs : values) {
					T entity = (T) serializer.deserialize(bs);
					if (entity != null) {
						result.add(entity);
					}
				}
				t.setStatus(Transaction.SUCCESS);
				return result;
			} else {
				t.setStatus(Transaction.SUCCESS);
				return null;
			}
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:hvals key:" + key, e);
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
	 * @title: set
	 * @description: 设置实体类型value
	 * @param keyT
	 * @param entity
	 * @return 总是返回"OK",因为 SET 不可能失败。
	 * @throws
	 */
	public <T> String set(final String keyT, T entity) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("set T", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("set T",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] bvalue = serializer.serialize(entity);
			String ret = commands.set(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:set key:" + key, e);
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
	 * @title: setex
	 * @description: 设置实体类型value，并将 key 的生存时间设为 seconds (以秒为单位) 。如果 key 已经存在，
	 *               SETEX 命令将覆写旧值。
	 * @param keyT
	 * @param expire
	 *            单位：秒
	 * @param entity
	 * @return 设置成功时返回 OK 。
	 * @throws
	 */
	public <T> String setex(final String keyT, int expire, T entity) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("setex T", key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("setex T",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] bvalue = serializer.serialize(entity);
			String ret = commands.setex(bkey, expire, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:setex key:" + key, e);
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
	 * @title: lindexE
	 * @description: 返回列表 key 中，下标为 index 的元素。
	 * @param keyT
	 * @param index
	 * @return 列表中下标为 index 的元素。如果 index 参数的值不在列表的区间范围内(out of range)，返回null 。
	 * @throws JedisDataException
	 *             如果 key 不是列表类型，抛出异常。
	 */
	@SuppressWarnings("unchecked")
	public <E> E lindexE(Class<E> memberClass, final String keyT, int index) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("lindexE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lindexE",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] value = commands.lindex(bkey, index);
			pool.returnResourceObject(commands);
			E result = (E) serializer.deserialize(value);
			t.setStatus(Transaction.SUCCESS);
			return result;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lindexE key:" + key, e);
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
	 * @title: lpop
	 * @description: 移除并返回列表 key 的头元素。
	 * @param keyT
	 * @return 列表的头元素。当 key 不存在时，返回 null 。
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public <E> E lpopE(Class<E> memberClass, final String keyT) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lpopE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lpopE",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] value = commands.lpop(bkey);
			pool.returnResourceObject(commands);
			E result = (E) serializer.deserialize(value);
			t.setStatus(Transaction.SUCCESS);
			return result;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lpopE key:" + key, e);
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
	 * @title: lpushE
	 * @description: 将一个或多个值 value 插入到列表 key 的表头,如果有多个 value 值，那么各个 value
	 *               值按从左到右的顺序依次插入到表头.如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
	 * @param keyT
	 * @param members
	 * @return 执行 LPUSH 命令后，列表的长度。
	 * @throws JedisDataException
	 *             当 key 存在但不是列表类型时，抛出异常。
	 */
	public <E> Long lpushE(final String keyT, E... members) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lpushE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lpushE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[][] bvalue = new byte[members.length][];
			for (int i = 0; i < bvalue.length; i++) {
				bvalue[i] = serializer.serialize(members[i]);
			}
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.lpush(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lpushE key:" + key, e);
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
	 * @title: lpushLE
	 * @description: 将一个或多个值 value 插入到列表 key 的表头,如果有多个 value 值，那么各个 value
	 *               值按从左到右的顺序依次插入到表头.如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
	 * @param keyT
	 * @param members
	 * @return 执行 LPUSH 命令后，列表的长度。
	 * @throws JedisDataException
	 *             当 key 存在但不是列表类型时，抛出异常。
	 */
	public <E> Long lpushLE(final String keyT, List<E> members) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lpushLE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lpushLE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[][] bvalue = new byte[members.size()][];
			for (int i = 0; i < bvalue.length; i++) {
				bvalue[i] = serializer.serialize(members.get(i));
			}
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.lpush(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lpushLE key:" + key, e);
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
	 * @title: lpushxE
	 * @description: 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。和 LPUSH 命令相反，当
	 *               key 不存在时， LPUSHX 命令什么也不做。
	 * @param keyT
	 * @param member
	 * @return LPUSHX 命令执行之后，表的长度。
	 * @throws
	 */
	public <E> Long lpushxE(final String keyT, E member) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lpushxE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lpushxE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] bvalue = serializer.serialize(member);
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.lpushx(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lpushxE key:" + key, e);
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
	 * @title: rpush
	 * @description: 将一个或多个值 value 插入到列表 key 的表尾(最右边)。如果有多个 value 值，那么各个 value
	 *               值按从左到右的顺序依次插入到表尾.如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
	 * @param keyT
	 * @param members
	 * @return 执行 RPUSH 操作后，表的长度。
	 * @throws JedisDataException
	 *             当 key 存在但不是列表类型时，抛出异常。
	 */
	public <E> Long rpushE(final String keyT, E... members) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("rpushE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("rpushE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[][] bvalue = new byte[members.length][];
			for (int i = 0; i < bvalue.length; i++) {
				bvalue[i] = serializer.serialize(members[i]);
			}
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.rpush(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:rpushE key:" + key, e);
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
	 * @title: rpushLE
	 * @description: 将一个或多个值 value 插入到列表 key 的表尾(最右边)。如果有多个 value 值，那么各个 value
	 *               值按从左到右的顺序依次插入到表尾.如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
	 * @param keyT
	 * @param members
	 * @return 执行 RPUSH 操作后，表的长度。
	 * @throws JedisDataException
	 *             当 key 存在但不是列表类型时，抛出异常。
	 */
	public <E> Long rpushLE(final String keyT, List<E> members) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("rpushLE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("rpushLE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[][] bvalue = new byte[members.size()][];
			for (int i = 0; i < bvalue.length; i++) {
				bvalue[i] = serializer.serialize(members.get(i));
			}
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.rpush(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:rpushLE key:" + key, e);
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
	 * @title: rpushx
	 * @description: 将值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。和 RPUSH 命令相反，当
	 *               key 不存在时， RPUSHX 命令什么也不做。
	 * @param keyT
	 * @param member
	 * @return RPUSHX 命令执行之后，表的长度。
	 * @throws
	 */
	public <E> Long rpushx(final String keyT, E member) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("rpushx E", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("rpushx E",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] bvalue = serializer.serialize(member);
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.rpushx(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:rpushx key:" + key, e);
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
	 * @title: lset
	 * @description: 将列表 key 下标为 index 的元素的值设置为 value 。
	 * @param keyT
	 * @param index
	 * @param member
	 * @return 操作成功返回 ok
	 * @throws JedisDataException
	 *             当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，抛出异常。
	 */
	public <E> String lsetE(final String keyT, int index, E member) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("lsetE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lsetE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[] bvalue = serializer.serialize(member);
			commands = (BinaryJedisCommands) pool.getResource();
			String ret = commands.lset(bkey, index, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lsetE key:" + key, e);
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
	 *
	 * @title: lrangeE
	 * @description: * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。下标(index)参数
	 *               start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1
	 *               表示列表的第二个元素，以此类推。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2
	 *               表示列表的倒数第二个元素，以此类推。 超出范围的下标值不会引起错误。 如果 start 下标比列表的最大下标 end
	 *               ( LLEN list 减去 1 )还要大，或者 start > stop ， LRANGE 返回一个空列表。 如果
	 *               stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
	 * @param memberClass
	 * @param keyT
	 * @param start
	 * @param end
	 * @return 个列表，包含指定区间内的元素。
	 * @throws JedisDataException
	 *             当 key 存在但不是列表类型时，抛出异常。
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> lrangeE(Class<E> memberClass, final String keyT,
			int start, int end) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("lrangeE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("lrangeE",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);

			List<byte[]> bvalue = commands.lrange(bkey, start, end);
			pool.returnResourceObject(commands);
			if (bvalue != null && bvalue.size() != 0) {
				List<E> list = new ArrayList<E>();
				for (byte[] bs : bvalue) {
					list.add((E) serializer.deserialize(bs));
				}
				t.setStatus(Transaction.SUCCESS);
				return list;
			}
			t.setStatus(Transaction.SUCCESS);
			return null;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:lrangeE key:" + key, e);
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
	 * @title: rpopE
	 * @description: 移除并返回列表 key 的尾元素。
	 * @param memberClass
	 * @param keyT
	 * @return 列表的尾元素。当 key 不存在时，返回 null 。
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public <E> E rpopE(Class<E> memberClass, final String keyT) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("rpopE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("rpopE",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			E ret = (E) serializer.deserialize(commands.rpop(bkey));
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:rpopE key:" + key, e);
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
	 * @title: saddE
	 * @description: 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。假如 key
	 *               不存在，则创建一个只包含 member 元素作成员的集合。
	 * @param keyT
	 * @param members
	 * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 * @throws JedisDataException
	 *             当 key 不是集合类型时，抛出异常。
	 */
	public <E> Long saddE(final String keyT, E... members) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("saddE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("saddE",keyT,logMessage,pool);
		try {
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			byte[][] bvalue = new byte[members.length][];
			for (int i = 0; i < bvalue.length; i++) {
				bvalue[i] = serializer.serialize(members[i]);
			}
			commands = (BinaryJedisCommands) pool.getResource();
			Long ret = commands.sadd(bkey, bvalue);
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:saddE key:" + key, e);
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
	 * @title: smembersE
	 * @description: 返回集合 key 中的所有成员。
	 * @param memberClass
	 * @param keyT
	 * @return 集合中的所有成员。
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public <E> Set<E> smembersE(Class<E> memberClass, final String keyT) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("smembersE", key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("smembersE",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			Set<byte[]> bvalue = commands.smembers(bkey);
			pool.returnResourceObject(commands);
			if (bvalue != null && bvalue.size() != 0) {
				Set<E> set = new HashSet<E>();
				for (byte[] bs : bvalue) {
					set.add((E) serializer.deserialize(bs));
				}
				t.setStatus(Transaction.SUCCESS);
				return set;
			}
			t.setStatus(Transaction.SUCCESS);
			return null;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:smembersE key:" + key, e);
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
	 * @title: spopE
	 * @description: 移除并返回集合中的一个随机元素。
	 * @param memberClass
	 * @param keyT
	 * @return 被移除的随机元素。当 key 不存在或 key 是空集时，返回 null 。
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public <E> E spopE(Class<E> memberClass, final String keyT) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getWritePool();
		Transaction t = catNewTransaction("spopE", key, pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("spopE",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			E ret = (E) serializer.deserialize(commands.spop(bkey));
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:spopE key:" + key, e);
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
	 * @title: srandmember
	 * @description: 返回集合中的一个随机元素。
	 * @param memberClass
	 * @param keyT
	 * @return 如果集合为空，返回null 。
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public <E> E srandmember(Class<E> memberClass, final String keyT) {
		String key = buildKey(keyT);
		BinaryJedisCommands commands = null;
		Pool pool = Router.inst.getReadPool();
		Transaction t = catNewTransaction("srandmember E",key,pool);
		LogMessage logMessage = new LogMessage();
		logMethodStart("srandmember E",keyT,logMessage,pool);
		try {
			commands = (BinaryJedisCommands) pool.getResource();
			byte[] bkey = key.getBytes(Protocol.CHARSET);
			E ret = (E) serializer.deserialize(commands.srandmember(bkey));
			pool.returnResourceObject(commands);
			t.setStatus(Transaction.SUCCESS);
			return ret;
		} catch (JedisException e){
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis JedisException error ", e);
			if (commands != null) {
				pool.returnBrokenResource(commands);
			}
			throw new JedisException(e);
		} catch (Exception e) {
			logMethodError(logMessage,e);
			t.setStatus(e);
			log.error("redis error method:srandmember key:" + key, e);
			if (commands != null) {
				pool.returnResourceObject(commands);
			}
			throw new JedisException(e);
		}finally {
			logMethodEnd(logMessage);
			t.complete();
		}
	}

	protected String buildKey(final String keyT) {
		if (StringUtils.isBlank(keyT)) {
			throw new JedisException("key is null");
		} else if (keyPrefix != null) {
			return keyPrefix + keyT;
		} else {
			StringBuffer sb = new StringBuffer();
			keyPrefix = sb.append(prefix).append("_").toString();
			return sb.append(keyT).toString();
		}
	}

	protected String getOriginalKey(final String keyT) {
		if (StringUtils.isBlank(keyT)) {
			throw new JedisException("key is null");
		}

		return keyT.substring((keyPrefix + "_").length());
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	protected void logMethodStart(String method,String keyT,LogMessage logMessage,Pool pool){
		if (remoteLogger.isInfoEnabled()) {
			MonitorThreadLocal local = MonitorThreadLocal.get();

			if (local != null) {
				logMessage.setId(local.getUuid());
				logMessage.setLevel(local.getLevel() + 1);
				local.nextIndex();
				logMessage.setIndex(local.getIndex());
				logMessage.setParam(new StringBuilder().append("key：").append(keyT).toString());
				logMessage.setRequestIP(new StringBuilder().append(Router.inst.getAddressAndPortByPool(pool)).toString());
				logMessage.setClassName("Cache");
				logMessage.setMethod(method);
				logMessage.setType(Constants.CACHE_METHOD_START);
				logMessage.setTime(System.currentTimeMillis());
				remoteLogger.info(JSON.toJSONString(logMessage));
			}

		}
	}

	protected void logMethodError(LogMessage logMessage,Exception e){
		if (remoteLogger.isInfoEnabled()) {
			MonitorThreadLocal local = MonitorThreadLocal.get();
			if (local != null) {
				local.nextIndex();
				logMessage.setIndex(local.getIndex());
				logMessage.setType(Constants.CACHE_METHOD_ERROR);
				logMessage.setTime(System.currentTimeMillis());
				logMessage.setErrMsg(e.getMessage());
				remoteLogger.info(JSON.toJSONString(logMessage));
				logMessage.setErrMsg(null);
			}
		}
	}

	protected void logMethodEnd(LogMessage logMessage){
		if (remoteLogger.isInfoEnabled()) {
			MonitorThreadLocal local = MonitorThreadLocal.get();
			if (local != null) {
				local.nextIndex();
				logMessage.setIndex(local.getIndex());
				logMessage.setType(Constants.CACHE_METHOD_END);
				logMessage.setTime(System.currentTimeMillis());
				remoteLogger.info(JSON.toJSONString(logMessage));
			}
		}
	}

	protected String getKeyStr(String... keys){
		StringBuffer sb = new StringBuffer();
		for(String s : keys){
			sb.append(s).append(",");
		}
		return sb.toString();
	}

	protected <T> String getKeyStr(Map<String, T> keysvalues){
		StringBuffer sb = new StringBuffer();
		for(String s : keysvalues.keySet()){
			sb.append(s).append(",");
		}
		return sb.toString();
	}
}
