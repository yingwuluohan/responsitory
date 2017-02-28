package com.common.utils.redis;

import com.common.utils.spring.SpringContextUtils;
import com.fang.common.project.KooJedisClient;

/**
 * Created by  on 2017/2/28.
 */
public class CacheTools {
    private CacheTools(){}
    static KooJedisClient client = SpringContextUtils.getBean("redisClient", KooJedisClient.class);


    /**
      *
     */
    public static <T> void addCache(String key, T ot) {
        try {
            //30分钟无操作失效
            client.setex(key, 120 * 60, ot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
