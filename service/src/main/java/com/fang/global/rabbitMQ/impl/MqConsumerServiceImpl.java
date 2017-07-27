package com.fang.global.rabbitMQ.impl;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

/**
 * Created by fn on 2017/7/26.
 */
public class MqConsumerServiceImpl  implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String entity = message.getBody().toString();
        System.out.println( entity );
    }


}
