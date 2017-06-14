package com.fang.global.kafka.impl;

import com.alibaba.fastjson.JSONObject;
import com.fang.service.kafka.KafkaConsumerServer;
import com.modle.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.kafka.listener.MessageListener;

/**
 * Created by fn on 2017/6/8.
 */
public class KafkaConsumerServerImpl {//implements KafkaConsumerServer ,MessageListener<Integer,String> {




   // @Override
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
        try{
            String userJson = consumerRecord.value();
            User user = null;
            if( null != userJson ){
                user = (User) JSONObject.parseObject( userJson, User.class);
                if( null != user ){
                    System.out.println( " 用户信息：" + user.getUserId());
                    System.out.println( " 用户信息：" + user.getEmail());
                }
            }

        }catch ( Exception e ){
            e.printStackTrace();
        }

    }
}
