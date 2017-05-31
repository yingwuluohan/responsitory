package com.netty.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


import java.net.InetSocketAddress;



/**
 * Created by fn on 2017/5/17.
 */
public final class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
        }
    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
             Bootstrap b = new Bootstrap();
             b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
            .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addFirst(new EchoClientHandler());
                     ch.pipeline().addFirst(new EchoClientHandler2());

                 }
             });
                 ChannelFuture f = b.connect().sync();
                 f.channel().closeFuture().sync();
            } finally {
            group.shutdownGracefully().sync();
            }
        }
    public static void main(String[] args) throws Exception {
       new EchoClient("localhost", 8088).start();
     }
}
