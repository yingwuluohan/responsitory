package com.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by fn on 2017/5/17.
 */

public class EchoServerHandler  extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println(  "Server received: " + in.toString(CharsetUtil.UTF_8)); //  将消息记录到控制台

        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        String resultStr = new String(result1);
        System.out.println("服务端handler获得信息1111111111111:" + resultStr);

    }

    /**
     * 客户端链接到服务器后被调用
     * @param ctx
     */
    @Override
    public void channelActive( ChannelHandlerContext ctx){
        System.out.println( "客户端链接到服务器后被调用" );
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
       // ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);//将未决消息冲刷到远程节点，并且关闭该Channel
        System.out.println( "服务端 handler:" + ctx.toString() );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();//打印异常栈跟踪
        ctx.close();//关闭该Channel
    }
}
