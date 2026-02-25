package com.notificationmanagement.dtos;

import com.notificationmanagement.models.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NotificationTemplateRequest {

	@NotBlank(message = "Template name is required")
	@Size(max = 100)
	private String name;

	@NotNull(message = "Notification type is required")
	private NotificationType type;

	@Size(max = 255)
	private String subject;

	@NotBlank(message = "Body is required")
	private String body;

	private Boolean isActive = true;

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