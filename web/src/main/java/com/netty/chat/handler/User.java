package com.netty.chat.handler;

import java.io.Serializable;

public class User implements Serializable {
	private int userId;
	private String userName;
	private String passwd;

	public User() {
	}

	public User(int userId, String userName, String passwd) {
		this.userId = userId;
		this.userName = userName;
		this.passwd = passwd;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
