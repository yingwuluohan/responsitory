package com.fang.controller.testcontroller;


import com.fang.service.CacheToolsService;
import com.modle.User;
import com.fang.service.DemoService;
import com.fang.service.TestRedisOperaterService;
import com.modle.UserInfo;
import com.modle.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    @Autowired
    private CacheToolsService cacheToolsService;


    @RequestMapping( value="/go" ,method= RequestMethod.GET )
    public String goTest(HttpServletRequest request , HttpServletResponse response){
        String result = demoService.findUserName();
        return "test";
    }
    @RequestMapping(value = "/atrribuilt"  , method = RequestMethod.GET )
    public String atrribuilt(@ModelAttribute UserInfo userInfo ){

        System.out.println( "获取属性:" + userInfo );
        return "";
    }
    @RequestMapping("/manage_{examId}_{studentId}")
    public String start(@PathVariable("examId") int examId,
                        @PathVariable("studentId") int studentId,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws Exception
    {
        //先判断examId 是否存在
        System.out.println( "获取参数:" + examId + ", " + studentId );
        return "/index";
    }

    @RequestMapping( value="/update" ,method= RequestMethod.GET )
    public String updateDb(){
        String result = demoService.updateUserName( "108" );
        return "test";
    }

    @RequestMapping( value = "/pageFind" ,method = RequestMethod.GET )
    public String pageFind(HttpServletRequest request , HttpServletResponse response ){
        String pageNo = request.getParameter( "pageNo" );
        if(StringUtils.isEmpty( pageNo )){
            pageNo = "0";
        }
        Page page = new Page();
        page.setPageNo( new Integer( pageNo ));
        page.setPageSize( 10 );
        List< User> list = demoService.findUserInfo( page );
        return "test";
    }

    @RequestMapping( value="/redis" ,method= RequestMethod.GET )
    public String redisTest(){
        User user = new User();
        user.setUserName( "testHightFilder111" );
        user.setCityName( "beijing22tes111t");
        cacheToolsService.addCacheForever("testHightFilder111" ,user );
        //testRedisOperaterService.add( user );
        return "/test";
    }


    @RequestMapping( value="/getRedis" ,method= RequestMethod.GET )
    public String getRedisValue(){
        String key = "beijing";
        String  value = testRedisOperaterService.deserializerValue( key );
        System.out.println( value );
        return "/test";
    }

    @RequestMapping( value="/deleteRedis" ,method= RequestMethod.GET )
    public String deleteRedis(){
        String key = "beijing";
        testRedisOperaterService.deleteRedis( key );
        return "/test";
    }


}
