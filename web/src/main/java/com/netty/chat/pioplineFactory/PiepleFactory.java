package com.netty.chat.pioplineFactory;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

public class PiepleFactory implements ChannelPipelineFactory{

	private ChannelHandler ch;
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
	
		ChannelPipeline c=Channels.pipeline();
		c.addLast("handler", ch);
		return c;
	}
	
	public PiepleFactory(ChannelHandler ch) {
		
		this.ch=ch;
	}
}
