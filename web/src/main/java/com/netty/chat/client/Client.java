package com.netty.chat.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.netty.chat.handler.ClientHandler;
import com.netty.chat.handler.MessageInfo;
import com.netty.chat.handler.User;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;



public class Client extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;
	private User user = null;
	JTextArea showText = this.getJTextArea("消息显示", 20, 32, 300, 160);
	JList<String> list = new JList<String>();
	JScrollPane jsPane = new JScrollPane();
	JTextArea sendText = this.getJTextArea("消息发送", 20, 260, 300, 50);
	JLabel idLabel = new JLabel("编号：");
	JTextField idText = new JTextField();
	JLabel nameLabel = new JLabel("账号");
	JTextField nameText = new JTextField();
	JLabel passLabel = new JLabel("密码：");
	JPasswordField passText = new JPasswordField();
	JButton login = this.getJButton("登陆", 85, 320, 70, 50);
	JButton sendMessage = this.getJButton("发送", 85, 320, 70, 50);
	JButton cancle = this.getJButton("取消", 160, 320, 70, 50);

	public Client() {
		this.addWindowListener(this);
	}

	ClientHandler handler;

	public ClientHandler getHandler() {
		return handler;
	}

	public void setHandler(ClientHandler handler) {
		this.handler = handler;
	}

	public void createConnection() {

		ChannelFactory cf = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ClientBootstrap boot = new ClientBootstrap(cf);
		handler = new ClientHandler(showText, list);

		boot.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						// 设置对象编码和解码
						new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers
								.cacheDisabled(getClass().getClassLoader())),
						handler);
			}
		});
		boot.setOption("tcpNoDelay", true);
		boot.setOption("keepAlive", true);
		boot.connect(new InetSocketAddress("localhost", 9999));
	}

	public void createFrame() {

		this.setTitle("局域网聊天");
		setVisible(true);
		pack();
		setLocation(350, 200);
		idLabel.setBounds(20, 230, 40, 20);
		idLabel.setVisible(true);
		idText.setBounds(60, 230, 60, 20);
		idText.setVisible(true);
		nameLabel.setBounds(120, 230, 40, 20);
		nameLabel.setVisible(true);
		nameText.setBounds(160, 230, 60, 20);
		nameText.setVisible(true);
		passLabel.setBounds(220, 230, 40, 20);
		passLabel.setVisible(true);
		passText.setBounds(260, 230, 60, 20);
		passText.setVisible(true);

		getContentPane().add(showText);
		getContentPane().add(sendText);
		getContentPane().add(idLabel);
		getContentPane().add(nameLabel);
		getContentPane().add(passLabel);
		getContentPane().add(idText);
		getContentPane().add(nameText);
		getContentPane().add(passText);
		sendText.setVisible(false);
		sendMessage.setVisible(false);

		// list.setVisibleRowCount(3);
		jsPane.setBounds(20, 195, 300, 60);
		jsPane.add(list);
		jsPane.setVisible(false);
		list.setBounds(0, 0, 300, 60);
		list.setBackground(Color.GREEN);
		list.setVisible(false);

		getContentPane().add(login);
		getContentPane().add(jsPane);
		getContentPane().add(sendMessage);
		getContentPane().add(cancle);
		login.addActionListener(new LoginAction());
		sendMessage.addActionListener(new SendMessageAction());
		cancle.addActionListener(new StopSendListener());
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		setSize(350, 400);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// 发送消息处理
	class SendMessageAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Channel channel = handler.getChannel();
			if (channel != null) {
				showText.setText(showText.getText() == null ? "" : showText
						.getText()
						+ user.getUserName()
						+ "say:"
						+ sendText.getText() + "\n");
				List<String> toUsers = new ArrayList<String>();
				for (Object obj : list.getSelectedValues()) {
					toUsers.add((String) obj);
				}
				MessageInfo mess = new MessageInfo(user.getUserName(), toUsers,
						sendText.getText());
				channel.write(mess);
				sendText.setText("");
			}
		}
	}

	// 登陆处理
	class LoginAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Channel channel = handler.getChannel();
			if (channel != null) {
				user = new User(Integer.parseInt(idText.getText()),
						nameText.getText(), passText.getPassword().toString());
				channel.write(user);
				idLabel.setVisible(false);
				idText.setVisible(false);
				nameLabel.setVisible(false);
				nameText.setVisible(false);
				passLabel.setVisible(false);
				passText.setVisible(false);
				login.setVisible(false);
				sendMessage.setVisible(true);
				sendText.setVisible(true);
				list.setVisible(true);
				jsPane.setVisible(true);
				showText.setText("");
			}
		}
	}

	class StopSendListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			sendText.setText("");
		}
	}

	public JLabel getJlabel(String text, int... location) {

		JLabel label = new JLabel(text);
		label.setFont(new Font(label.getText(), 0, 18));
		label.setBounds(location[0], location[1], location[2], location[3]);
		return label;
	}

	public JTextField getJTextField(String name, int... location) {

		JTextField text = null;
		switch (location[4]) {
		case 1:
			text = new JTextField();
			break;
		default:
			text = new JPasswordField();
			break;
		}
		text.setBounds(location[0], location[1], location[2], location[3]);
		text.setBorder(BorderFactory.createLineBorder(new Color(51,
				255, 51)));
		return text;
	}

	public JTextArea getJTextArea(String name, int... location) {

		JTextArea areaText = new JTextArea();
		areaText.setBounds(location[0], location[1], location[2], location[3]);
		areaText.setBorder(BorderFactory.createLineBorder(new Color(
				51, 255, 51)));
		return areaText;
	}

	public JButton getJButton(String text, int... location) {

		JButton button = new JButton(text);
		button.setBounds(location[0], location[1], location[2], location[3]);
		return button;
	}

	public static void main(String[] args) {

		Client c = new Client();
		c.createFrame();
		c.createConnection();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		int option = JOptionPane.showConfirmDialog(this, "确定", "取消",
				JOptionPane.OK_CANCEL_OPTION);
		if (JOptionPane.OK_OPTION == option) {
			// 关闭连接
			handler.getChannel().disconnect();
			System.exit(1);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
