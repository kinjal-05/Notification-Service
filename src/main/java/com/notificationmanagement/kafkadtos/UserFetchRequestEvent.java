package com.notificationmanagement.kafkadtos;

public class UserFetchRequestEvent {
	private Long requestId;

	public UserFetchRequestEvent() {
	}

	public UserFetchRequestEvent(Long requestId) {
		this.requestId = requestId;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

}
