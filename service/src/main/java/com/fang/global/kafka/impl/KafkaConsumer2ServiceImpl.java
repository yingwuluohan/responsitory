package com.fang.global.kafka.impl;

import com.alibaba.fastjson.JSONObject;
import com.fang.service.kafka.KafkaConsumerServer;
import com.modle.User;
import org.omg.CORBA.Object;

import java.util.Map;

/**
 * Created by fn on 2017/6/13.
 */
public class KafkaConsumer2ServiceImpl implements KafkaConsumerServer {
    private String TOPIC;


    private String statisticsDirectory;
    private String chartsDirectory;

    public String getTOPIC() {
        return TOPIC;
    }

    public void setTOPIC(String TOPIC) {
        this.TOPIC = TOPIC;
    }

    public void setStatisticsDirectory(String statisticsDirectory) {
        this.statisticsDirectory = statisticsDirectory;
    }

    public String getStatisticsDirectory() {
        return statisticsDirectory;
    }

    public void setChartsDirectory(String chartsDirectory) {
        this.chartsDirectory = chartsDirectory;
    }

    public String getChartsDirectory() {
        return chartsDirectory;
    }
    public void consumerKafkaMessage(Object object ){
        try{
            User user = null;
            if( null != object ){
                user = (User) JSONObject.parseObject( object.toString(), User.class);
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
