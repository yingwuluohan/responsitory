package com.netty.upload;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.quartz.Scheduler;

/**
 * Created by fn on 2017/5/24.
 */
public class DBServer extends Thread {

    //单实例
    private static DBServer dbServer = null;

    //定时调度的周期实例
    private static Scheduler sched = null;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    //创建实例
    public static DBServer newBuild() {
        if(dbServer == null) {
            dbServer = new DBServer();
        }
        return dbServer;
    }
    public static void main( String[] args ){
        new Thread( new DBServer() ).start();
    }

    public void run() {
        try {
            startServer();
        } catch(Exception e) {
            System.out.println("数据服务启动出现异常："+e.toString());
            e.printStackTrace();
        }
    }

    private void startServer() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.option(ChannelOption.SO_TIMEOUT, 60000);
            b.option(ChannelOption.SO_SNDBUF, 1048576*200);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new DBServerInitializer());

            // 服务器绑定端口监听
           // ChannelFuture f = b.bind(DBConfig.curHost.getIp(), DBConfig.curHost.getPort()).sync();
            ChannelFuture f = b.bind( "127.0.0.1", 8088 ).sync();

            System.out.println("数据服务：*****启动完成...");
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
