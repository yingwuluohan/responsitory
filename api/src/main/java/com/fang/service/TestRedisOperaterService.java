package com.fang.service;


import com.modle.User;

/**
 * Created by fn on 2017/2/14.
 */
public interface TestRedisOperaterService {

    public <T> T addForlong( final String key , Class<T> classes );
    boolean add(final User user);

    String deserializerValue(String key);

    boolean deleteRedis(String key);
}
