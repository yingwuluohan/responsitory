package com.common.utils.redis;

import com.common.utils.spring.SpringContextUtils;
import com.fang.common.project.KooJedisClient;

/**
 * Created by fn on 2017/4/26.
 */
public class RadisCacheUtil {


    public static <T> T getCacheNoTime(String key,Class<T> classes) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        T t = client.get(key, classes);
        return t;
    }


    public static <T> void addCache(String key, int time, T ot) {
        KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);
        try {
            //time秒钟无操作失效
            client.setex(key, time, ot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
