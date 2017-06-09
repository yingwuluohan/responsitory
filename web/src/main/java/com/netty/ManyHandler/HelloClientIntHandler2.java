package com.netty.ManyHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class HelloClientIntHandler2 extends ChannelInboundHandlerAdapter {
   // private static Logger   logger  = LoggerFactory.getLogger(HelloClientIntHandler2.class);
    private static Logger logger  = Logger.getLogger(HelloClientIntHandler2.class);
    @Override
    // 读取服务端的信息
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("HelloClientIntHandler.channelRead");
        System.out.println( "第二个客户端handler获得服务端信息 :");
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        result.release();
        System.out.println("Server said:" + new String(result1));
    }
    @Override
    // 当连接建立的时候向服务端发送消息 ，channelActive 事件当连接建立的时候会触发
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("HelloClientIntHandler.channelActive");
        System.out.println( "第二个dler给服务端发信息 ");
        String msg = "send client message 222222 !";
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(msg.getBytes());
        ctx.write(encoded);
        ctx.flush();
    }
}