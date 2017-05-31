package com.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by fn on 2017/5/17.
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println( "Usage: " + EchoServer.class.getSimpleName() + " ");
        }
          //设置端口值（如果端口参数的格式不正确，则抛出一个NumberFormatException）
        new EchoServer( 8088 ).start(); //调用服务器的start()方法
    }
    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();  //❷ 创建Server-Bootstrap
            b.group( bossGroup , workerGroup ).channel(NioServerSocketChannel.class).localAddress(port)
             .childHandler(new ChannelInitializer<Channel>() {
                 @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new EchoServerHandler());
                    }
                 });

            ChannelFuture f = b.bind().sync(); //❻ 异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成
            f.channel().closeFuture().sync();//❼ 获取Channel的CloseFuture，并且阻塞当前线程直到它完成
        } finally {
            bossGroup.shutdownGracefully().sync();// ❽ 关闭EventLoopGroup，释放所有的资源
            workerGroup.shutdownGracefully().sync();// ❽ 关闭EventLoopGroup，释放所有的资源
        }
    }
}
