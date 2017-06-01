package com.netty.chat.handler;

import java.io.Serializable;
import java.util.List;

public class MessageInfo implements Serializable {
	private String fromUser;

	private List<String> toUsers;
	private String message;

	public MessageInfo() {
	}

	public MessageInfo(String fromUser, List<String> toUsers, String message) {
		this.fromUser = fromUser;
		this.toUsers = toUsers;
		this.message = message;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public List<String> getToUsers() {
		return toUsers;
	}

	public void setToUsers(List<String> toUsers) {
		this.toUsers = toUsers;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
