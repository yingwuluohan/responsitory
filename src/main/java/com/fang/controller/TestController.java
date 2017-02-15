package com.fang.controller;

import com.common.utils.modle.User;
import com.fang.service.DemoService;
import com.fang.service.TestRedisOperaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by fn on 2017/2/9.
 */
@RequestMapping("/test")
@Controller
public class TestController {

    @Autowired
    private DemoService demoService;
    @Autowired
    private TestRedisOperaterService testRedisOperaterService;

    @RequestMapping( value="/go" ,method= RequestMethod.GET )
    public String goTest(){
        String result = demoService.findUserName();
        return "/test";
    }

    @RequestMapping( value="/redis" ,method= RequestMethod.GET )
    public String redisTest(){
        User user = new User();
        user.setUserName( "test22" );
        user.setCityName( "beijing222");
        testRedisOperaterService.add( user );
        return "/test";
    }

    /**
     * /test/getRedis
     * @return
     */
    @RequestMapping( value="/getRedis" ,method= RequestMethod.GET )
    public String getRedisValue(){
        String key = "beijing";
        String  value = testRedisOperaterService.deserializerValue( key );
        System.out.println( value );
        return "/test";
    }
    /**
     * /test/deleteRedis
     * @return
     */
    @RequestMapping( value="/deleteRedis" ,method= RequestMethod.GET )
    public String deleteRedis(){
        String key = "beijing";
        testRedisOperaterService.deleteRedis( key );
        return "/test";
    }

}
