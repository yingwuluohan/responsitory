package com.fang.common.project.redis;

import com.common.utils.modle.User;
import com.common.utils.redis.AbstractBaseRedisDao;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created by   on 2017/2/14.
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
