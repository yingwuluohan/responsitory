package com.netty.chat.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.netty.chat.handler.ServerHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;


public class ChartServer {

	public static void main(String[] args) {

		ChannelFactory cf = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ServerBootstrap boot = new ServerBootstrap(cf);
		boot.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						//设置对象编码和解码
						new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers
								.cacheDisabled(getClass().getClassLoader())),
						new ServerHandler());
			}
		});
		boot.setOption("child.tcpNoDelay", true);
		boot.setOption("child.keepAlive", true);
		boot.bind(new InetSocketAddress(9999));
	}

}
