package com.fang.global.kafka.impl;

import com.alibaba.fastjson.JSONObject;
import com.fang.service.kafka.KafkaProducerServer;
import com.modle.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * kafkaProducer模板
 *     使用此模板发送消息
 * @author wangb
 *
 */
@Service("kafkaProducerService")
public class KafkaProducerServerImpl implements KafkaProducerServer{
    @Autowired
    private KafkaTemplate< Integer , String > kafkaTemplate;
    @Override
    public void sendDefualtMessage(Object object) {
        try{
            if( null != object ){
                String  str = JSONObject.toJSONString(object);
                kafkaTemplate.sendDefault( str );
            }
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
}