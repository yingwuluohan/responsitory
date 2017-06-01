package com.netty.chat.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class ServerHandler extends SimpleChannelHandler {
	// map用于channel和具体的用户名绑定起来，可以根据具体业务实现认证信息和channel绑定
	static final Map<Channel, String> channelMap = Collections
			.synchronizedMap(new HashMap<Channel, String>());
	// set保存登陆的用户信息
	static final List<String> set = new ArrayList<String>();

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		// flag用于判断是否第一次连接，不是的话，将其设置为1，并记录在ctx中，目前还没认真拜读相关资料，了解ctx作用。待后期认真看下。
		int flag = 0;
		if (ctx.getAttachment() != null) {
			flag = Integer.parseInt(ctx.getAttachment().toString());
		}
		// flag为0时，将用户和其建立的channel绑定，否则获取用户发送过来的messageInfo，依据里面的toUsers去map里面找到用户所建立的channel，
		//通过对应的channel把信息发送到相应的用户。
		if (flag++ == 0) {
			User user = (User) e.getMessage();
			channelMap.put(e.getChannel(), user.getUserName());
			set.add(user.getUserName());
			for (Channel c : channelMap.keySet()) {
				c.write(set);
			}
			ctx.setAttachment(flag);
		} else {
			// System.out.println("user");
			MessageInfo mess = (MessageInfo) e.getMessage();
			for (String str : mess.getToUsers()) {
				//根据用户名获取，用户所建立的channel，并发送信息。
				Channel channel = getKey(channelMap, str);
				if (channel != null) {
					channel.write(mess.getFromUser() + " say:"
							+ mess.getMessage() + "\n");
				}
			}
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		set.remove(channelMap.get(e.getChannel()));
		channelMap.remove(e.getChannel());
		e.getChannel().close();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		String str = "请输入账号密码！\n";
		e.getChannel().write(str);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		set.remove(channelMap.get(e.getChannel()));
		channelMap.remove(e.getChannel());
		e.getChannel().close();
	}
//通过map的value值去获取相对应得key，这里即用户所建立的channel。
	public Channel getKey(Map<Channel, String> map, String value) {
		Channel key = null;
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			if (entry.getValue().equals(value)) {
				key = (Channel) entry.getKey();
			}
		}
		return key;
	}
}
