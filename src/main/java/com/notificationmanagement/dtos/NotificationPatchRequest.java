package com.notificationmanagement.dtos;

import com.notificationmanagement.models.NotificationStatus;
import jakarta.validation.constraints.Size;

public class NotificationPatchRequest {

	private NotificationStatus status;

	@Size(max = 2000, message = "Message cannot exceed 2000 characters")
	private String message;

	@Size(max = 255, message = "Subject cannot exceed 255 characters")
	private String subject;

	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
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
}
