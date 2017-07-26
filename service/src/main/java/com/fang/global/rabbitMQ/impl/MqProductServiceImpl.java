package com.fang.global.rabbitMQ.impl;

import com.alibaba.fastjson.JSONObject;
import com.fang.service.rabbitMq.MessageEntity;
import com.fang.service.rabbitMq.MqProductService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fn on 2017/7/26.
 */
public class MqProductServiceImpl implements MqProductService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    private String taskWordQueue="taskWordQueue";
    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }


    @Override
    public void sendMessage(Object obj) {
        if( null != obj ){
            MessageEntity logEntity = ( MessageEntity ) obj;
            String str = JSONObject.toJSONString( logEntity );
            amqpTemplate.convertAndSend( taskWordQueue , str );
        }
    }
}
