package com.common.utils.redis;

import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created by fn on 2017/2/14.
 */
public abstract class AbstractBaseRedisDao<K, V> {

    @Autowired
    protected RedisTemplate<K, V> redisTemplate;

    /**
     * 设置redisTemplate
     * @param redisTemplate the redisTemplate to set
     */
    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取 RedisSerializer
     * <br>------------------------------<br>
     */
    protected RedisSerializer<String> getRedisSerializer() {
        return redisTemplate.getStringSerializer();
    }

    public <T> T  getDeserializerValue(byte[] value){
        RedisSerializer<?> serializer = redisTemplate.getDefaultSerializer();
        RedisSerializer<java.lang.Object> serializer2 = new JdkSerializationRedisSerializer();
        T result = (T) serializer2.deserialize(value);
        return result;
    }

    public <T> T getRedisValue( T t ){

        return t;
    }

}
