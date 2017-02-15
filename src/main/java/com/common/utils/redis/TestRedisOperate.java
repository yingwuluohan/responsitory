package com.common.utils.redis;

import com.common.utils.modle.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created by fn on 2017/2/14.
 */
public class TestRedisOperate extends AbstractBaseRedisDao<String, User> {

    public boolean add(final User user) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] key  = serializer.serialize(user.getId()+"");
                byte[] name = serializer.serialize(user.getUserName());
                return connection.setNX(key, name);
            }
        });
        return result;
    }
}
