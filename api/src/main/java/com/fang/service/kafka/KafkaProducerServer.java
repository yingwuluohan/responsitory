package com.fang.service.kafka;

import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;

/**
 * Created by fn on 2017/6/8.
 */
public interface KafkaProducerServer  {

     void sendDefualtMessage(String object) ;

}
