package com.notificationmanagement.dtos;

import com.notificationmanagement.models.NotificationType;
import jakarta.validation.constraints.Size;

public class NotificationTemplateUpdateRequest {

	@Size(max = 100, message = "Template name cannot exceed 100 characters")
	private String name;

	private NotificationType type;

	@Size(max = 255, message = "Subject cannot exceed 255 characters")
	private String subject;

	private String body;

	private Boolean isActive;

	// Getters and Setters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public NotificationType getType() { return type; }
	public void setType(NotificationType type) { this.type = type; }

	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }

	public String getBody() { return body; }
	public void setBody(String body) { this.body = body; }

	public Boolean getIsActive() { return isActive; }
	public void setIsActive(Boolean active) { isActive = active; }
}
