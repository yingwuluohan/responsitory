package com.fang.global.service.impl.utils;

import com.common.utils.spring.SpringContextUtils;
import com.fang.common.project.KooJedisClient;
import com.fang.service.CacheToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by  on 2017/2/28.
 */
@Service("cacheToolsService")
public class CacheToolsServiceImpl implements CacheToolsService {

    //static KooJedisClient clientSer = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
    @Autowired
    private KooJedisClient client;


    public  <T> void addCache(String key, int time, T ot) {
        try {
            //time秒钟无操作失效
            client.setex(key, time, ot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public <T> T getCache(String key, Class<T> classes) {
        T t = client.get(key, classes);
        if (t != null) {
            //重新更新为30分钟
            client.setex(key, 120 * 60, t);
        }
        return t;
    }
    public  void delCache(String key) {
            client.del(key);
    }
    public <T> T getCacheNoTime(String key,Class<T> classes) {
        T t = client.get(key, classes);
        return t;
    }
    public <T> void addCacheForever(String key, T ot) {
        KooJedisClient clientSer = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        clientSer.set(key, ot);


    }
}
