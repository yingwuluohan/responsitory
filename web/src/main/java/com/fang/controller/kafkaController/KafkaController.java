package com.fang.controller.kafkaController;

import com.fang.controller.chatController.WebsocketChatServer;
import com.fang.service.kafka.KafkaConsumerServer;
import com.fang.service.kafka.KafkaProducerServer;

import com.modle.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by fn on 2017/6/8.
 */
@RequestMapping("/kafka")
@Controller
public class KafkaController {

    private KafkaConsumerServer kafkaConsumerServer;

    @Autowired
    private KafkaProducerServer kafkaProducerServer;

    @RequestMapping( value="/init" ,method= RequestMethod.GET )
    public void initTest(){

            User user = new User();
            user.setUserId( 23543  );
            user.setUserName( "sdfg4et"  );
            user.setEmail( "dsk3w3@123"  );
            kafkaProducerServer.sendDefualtMessage( "111111111111" );


    }



}
