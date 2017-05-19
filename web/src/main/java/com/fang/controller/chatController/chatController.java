package com.fang.controller.chatController;

import com.alibaba.fastjson.JSONObject;
import com.chart.ClientService;
import com.fang.service.chart.ChartRoomService;
import com.fang.service.chart.ClientServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fn on 2017/5/19.
 */
@RequestMapping("/chat")
@Controller
public class ChatController {

    @Autowired
    private ChartRoomService chartRoomService;
    @Autowired
    private ClientServiceApi clientService;


    /**
     * 初始化聊天服务
     */
    @RequestMapping( value="/initChatServer" ,method= RequestMethod.GET )
    public void initChatServer(){

        chartRoomService.init( 19999 );
        ClientService.getInstance();
    }
    @RequestMapping( value="/goChat" ,method= RequestMethod.GET )
    public String goChat(){

        return "chat/clientWindow";
    }


    /**
     * 发送消息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping( value="/sendMessage" ,method= RequestMethod.POST )
    public String sendMessage(HttpServletRequest request , HttpServletResponse response ){
        clientService.sendMsg( "sdf3rrg" );

        JSONObject param = (JSONObject) JSONObject.toJSON(  new HashMap< String , String >().put( "sdfdd" , "sdf"));
        return param.toString();
    }

    /**
     * 接收消息
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping( value="/receiveMsg" ,method= RequestMethod.POST )
    public String receiveMsg(HttpServletRequest request , HttpServletResponse response ){
        String message = clientService.receiveMsg(  );

        JSONObject param = (JSONObject) JSONObject.toJSON( message );
        return param.toString();
    }
}
