package com.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by fn on 2017/5/10.
 */
public class TimeClient {

    public void conect( int port , String host ){
        EventLoopGroup group = new NioEventLoopGroup( );
        try{
            Bootstrap boot = new Bootstrap();
            Bootstrap handler = boot.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).
                    handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast( new TimeClientHandler() );
                        }


                    });
        }catch (Exception e ){}
    }


}
