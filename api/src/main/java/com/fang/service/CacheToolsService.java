package com.fang.service;

/**
 * Created by fn on 2017/3/1.
 */
public interface CacheToolsService  {

    <T> void addCache(String key, int time, T ot);
    <T> T getCache(String key, Class<T> classes);
    void delCache(String key);
    <T> void addCacheForever(String key, T ot);
    <T> T getCacheNoTime(String key,Class<T> classes);
}
