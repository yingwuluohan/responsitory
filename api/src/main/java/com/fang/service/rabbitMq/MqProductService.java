package com.fang.service.rabbitMq;

/**
 * Created by fn on 2017/7/26.
 */
public interface MqProductService < T > {
    /**
     * 生产数据
     * @param t
     */
    void sendMessage( T t );


}
