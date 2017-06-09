package com.netty.ManyHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.apache.log4j.Logger;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class OutboundHandler2 extends ChannelOutboundHandlerAdapter {
    private static Logger   logger  = Logger.getLogger(OutboundHandler2.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        logger.info("OutboundHandler2.write");
        System.out.println( "服务端给客户端发消息out222222222222222" );
        // 执行下一个OutboundHandler
        String content = " server the secend word good to you ";
        byte[] result1 = content.getBytes();
        ByteBuf encoded = ctx.alloc().buffer(4 * result1.length);
        encoded.writeBytes( result1 );
        ctx.write(encoded);
        ctx.flush();
        super.write(ctx, content , promise);
    }
}
