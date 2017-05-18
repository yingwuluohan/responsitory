package com.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * Created by fn on 2017/5/10.
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger( TimeClientHandler.class);

    private final ByteBuf firstMassege;

    public TimeClientHandler(){
        byte[] by =   "first netty".getBytes();
        firstMassege = Unpooled.buffer( by.length );
        firstMassege.writeBytes( by );
    }
    public void channelRead(ChannelHandlerContext channelHandlerContext , Object object ){
        ByteBuf bb = ( ByteBuf )object;
        byte[] request = new byte[ bb.readableBytes()];
        bb.readBytes( request );
        try {
            String body = new String( request , "UTF-8");
            System.out.println( body );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
