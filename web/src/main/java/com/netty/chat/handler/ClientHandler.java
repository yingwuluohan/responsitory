package com.netty.chat.handler;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

@ChannelPipelineCoverage("one")
public class ClientHandler extends SimpleChannelHandler {

	private Channel channel;
	// 消息显示文本域
	private JTextArea showText;
	// 在想用户列表显示控件
	private JList<String> list;
	// 在线用户列表
	private List<String> users = new ArrayList<String>();

	public ClientHandler(JTextArea showText, JList<String> list) {
		this.list = list;
		this.showText = showText;
	}

	public JTextArea getShowText() {
		return showText;
	}

	public void setShowText(JTextArea showText) {
		this.showText = showText;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		setChannel(e.getChannel());
		System.out.println("Connected server");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		// 判断传过的对象是string对象还是messageInfo对象，依据相应对象做处理
		String sendClientMessage = "";
		if (e.getMessage() instanceof String) {
			sendClientMessage = (String) e.getMessage();
			showText.setText(showText.getText() == null ? "" : showText
					.getText() + sendClientMessage);
			if (showText.getText().length() > 2000) {
				showText.setText("");
			}
		} else {
			users = (List<String>) e.getMessage();
			DefaultListModel<String> dlm = new DefaultListModel<String>();
			for (String str : users) {
				dlm.addElement(str);
			}
			list.setModel(dlm);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
	}

}
