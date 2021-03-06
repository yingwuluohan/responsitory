package com.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

/**
 * Created by fn on 2017/5/17.
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends  SimpleChannelInboundHandler<ByteBuf> {
    private ChannelFuture future = null;
    @Override
    public void channelActive(ChannelHandlerContext ctx ) {
        Channel channel = ctx.channel();
        if( channel.isActive()) {
           // channel.writeAndFlush( "channelActive*******************" ) ;
        }
        //当被通知Channel是活跃的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",  CharsetUtil.UTF_8));

        System.out.println( "客户端handler给服务端发信息*************************" );
        String msg = "i am client ,Are you ok?";
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(msg.getBytes());
        ctx.write(encoded);
        ctx.flush();
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        ctx.write( in );
        Channel channel = ctx.channel();
        if(channel.isActive() && channel.isWritable()) {
            channel.writeAndFlush( in );
            channel.writeAndFlush( "tstupload" ).awaitUninterruptibly();
        }
        //记录已接收消息的转储
        System.out.println( "Client received: " + in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,  //在发生异常时，记录错误并关闭Channel
            Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
