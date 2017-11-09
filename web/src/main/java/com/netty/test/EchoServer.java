package com.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;

/**
 * Created by fn on 2017/5/17.
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    /**
     * 零拷贝
     */
    public void zeroCopy(){
        byte[] bytes = new String("test").getBytes();
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
    }

    /**
     * 应用服务的QPS只是几百万,那么parentGroup只需要设置为2,childGroup设置为4
     *线程数一般都不是写死的,一种是设置到环境变量里,而更好的方式是在Java进程启动的时候,
     * 指定参数,将线程数设置进去。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        EventLoopGroup  parentGroup = new NioEventLoopGroup(2, new DefaultThreadFactory("server1", true));
        EventLoopGroup  childGroup = new NioEventLoopGroup(4, new DefaultThreadFactory("server2", true));

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
