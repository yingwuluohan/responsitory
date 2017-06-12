package com.fang.service.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

import java.util.Map;

/**
 * Created by fn on 2017/6/8.
 */
public interface KafkaConsumerServer {//extends MessageListener<String, String> {
    //public void onMessage(ConsumerRecord<String, String> record);

   // public void processMessage(Map<String, Map<Integer, Object>> msgs);
}
