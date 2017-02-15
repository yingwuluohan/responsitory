package com.fang.service;

import com.common.utils.modle.User;

/**
 * Created by fn on 2017/2/14.
 */
public interface TestRedisOperaterService {

    boolean add(final User user);

    String deserializerValue(String key);

    boolean deleteRedis(String key);
}
