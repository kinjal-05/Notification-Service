package com.notificationmanagement.dtos;

import java.time.LocalDateTime;

public class NotificationResponse {
	private Long id;
	private Long userId;
	private String message;
	private String subject;
	private String status;
	private LocalDateTime sentAt;

	public NotificationResponse(Long id, Long userId, String message,
	                            String subject, String status, LocalDateTime sentAt) {
		this.id = id;
		this.userId = userId;
		this.message = message;
		this.subject = subject;
		this.status = status;
		this.sentAt = sentAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
}
