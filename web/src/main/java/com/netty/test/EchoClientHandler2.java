package com.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

/**
 * Created by fn on 2017/5/17.
 */
@ChannelHandler.Sharable
public class EchoClientHandler2 extends  SimpleChannelInboundHandler<ByteBuf> {
    private ChannelFuture future = null;
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        if( channel.isActive()) {
            //channel.writeAndFlush( "EchoClientHandler2222*******************" ) ;
        }
        //当被通知Channel是活跃的时候，发送一条消息
        System.out.println( "客户端handler给服务端发信息2222222222222222" );
        String msg = "i am client ,this my secend time send news ";
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(msg.getBytes());
        ctx.write(encoded);
        ctx.flush();
        ctx.write(msg);
        ctx.fireChannelRead(msg);

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println( "Client received222222: " + in.toString(CharsetUtil.UTF_8));
        ctx.writeAndFlush( "channelRead22222方法**" );
        Channel channel = ctx.channel();
        //记录已接收消息的转储

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,  //在发生异常时，记录错误并关闭Channel
            Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
