package com.fang.service.impl;

import com.common.utils.modle.User;
import com.common.utils.redis.AbstractBaseRedisDao;
import com.fang.service.TestRedisOperaterService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * Created by fn on 2017/2/14.
 */
@Service
public class TestRedisOperaterServiceImpl extends AbstractBaseRedisDao<String, User> implements TestRedisOperaterService {

    public boolean add(final User user) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize(user.getCityName());
                byte[] name = serializer.serialize(user.getUserName());
                boolean result = true;//connection.setNX(key, name);
                connection.expire( key ,1000 * 60 * 2 );
                connection.setEx( key ,1000 * 60 * 2 , name );
                return result;
            }
        });
        return result;
    }

    public String deserializerValue( final String key){
        String result = redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection)throws DataAccessException {
                String str = "";
                try {
                    byte[] value1 = connection.get( key.getBytes("UTF-8") );
                    str = new String( value1 , "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return str;
            }
        });
        return result;
    }


    public boolean deleteRedis(final String key ){
        Boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                    long result = connection.del(key.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
            return true;
    }

}
