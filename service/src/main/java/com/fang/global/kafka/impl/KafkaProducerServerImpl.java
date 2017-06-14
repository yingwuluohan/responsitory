package com.fang.global.kafka.impl;

import com.alibaba.fastjson.JSONObject;
import com.fang.service.kafka.KafkaProducerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

/**
 * kafkaProducer模板
 *     使用此模板发送消息
 * @author wangb
 *
 */

public class KafkaProducerServerImpl implements KafkaProducerServer{

    @Autowired
    private MessageChannel kafkaProvider;

    @Override
    public void sendDefualtMessage(String object) {
        try{
            if( null != object ){
                kafkaProvider.send( MessageBuilder.withPayload( object ).build() );
            }
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
    public void setKafkaProvider(MessageChannel kafkaProvider) {
        this.kafkaProvider = kafkaProvider;
    }

    public MessageChannel getKafkaProvider() {
        return kafkaProvider;
    }
}