package com.fang.controller.testcontroller;


import com.fang.service.CacheToolsService;
import com.fang.service.DemoService;
import com.fang.service.TestRedisOperaterService;
import com.modle.User;
import com.modle.UserInfo;
import com.modle.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

            User user = demoService.findUser( "1325654556" );

            String result = demoService.findUserName();
            System.out.println( result );

        return "test";
    }

    /**
     * 参数封装成对象
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/atrribuilt"  , method = RequestMethod.GET )
    public String atrribuilt(@ModelAttribute UserInfo userInfo ){

        System.out.println( "获取属性:" + userInfo );
        return "";
    }
    /**
     * url路径传参
     * @param examId
     * @return
     * @throws Exception
     */
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

    /**
     * 接收独立的参数，
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public String list(@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,
                       @RequestParam(value = "platformId", required = false) String platformId,  Model view) {
        view.addAttribute("platformId", platformId);
        return "/admin/adminChannel/list";
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

    public static void main(String[] args) {
        int num = Runtime.getRuntime().availableProcessors();
        System.out.println( num );

        String str = "多看看代收款";
        String[] array = str.split("");
        for( String s : array )
        System.out.println( "resuot:" + s );

        byte d3 = 127; // 如果是byte d3 = 128;会报错
        byte d4 = -1;
        char c = (char) 16; // char不能识别负数，必须强制转换否则报错，即使强制转换之后，也无法识别
        System.out.println(c);
    }

}
