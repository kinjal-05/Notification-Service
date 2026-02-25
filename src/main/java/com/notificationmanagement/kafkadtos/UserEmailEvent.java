package com.notificationmanagement.kafkadtos;

import java.util.List;

public class UserEmailEvent {

	private Long requestId;
	private List<UserEmailDto> users;

	public UserEmailEvent() {
	}

	public UserEmailEvent(Long requestId, List<UserEmailDto> users) {
		this.requestId = requestId;
		this.users = users;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public List<UserEmailDto> getUsers() {
		return users;
	}

	public void setUsers(List<UserEmailDto> users) {
		this.users = users;
	}
}