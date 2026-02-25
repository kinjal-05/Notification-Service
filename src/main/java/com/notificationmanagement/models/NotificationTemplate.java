package com.notificationmanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_templates",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"name"})
		},
		indexes = {
				@Index(name = "idx_template_type", columnList = "type"),
				@Index(name = "idx_template_active", columnList = "isActive")
		}
)
public class NotificationTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Template name is required")
	@Size(max = 100, message = "Template name cannot exceed 100 characters")
	@Column(nullable = false, unique = true, length = 100)
	private String name;

	@NotNull(message = "Notification type is required")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;

	@Size(max = 255, message = "Subject cannot exceed 255 characters")
	@Column(length = 255)
	private String subject;

	@NotBlank(message = "Template body is required")
	@Column(nullable = false, columnDefinition = "TEXT")
	private String body;

	@NotNull(message = "Active flag must be provided")
	@Column(nullable = false)
	private Boolean isActive = true;

	// ✅ Timestamp fields
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	// Constructors
	public NotificationTemplate() {}

	public NotificationTemplate(String name, NotificationType type, String subject, String body, Boolean isActive) {
		this.name = name;
		this.type = type;
		this.subject = subject;
		this.body = body;
		this.isActive = isActive;
	}

	// ✅ Automatically set timestamps
	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;

		validateTemplate();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();

		validateTemplate();
	}

	// ✅ Business validation
	private void validateTemplate() {
		if (this.type == NotificationType.EMAIL &&
				(this.subject == null || this.subject.isBlank())) {
			throw new IllegalArgumentException("Subject is required for EMAIL templates");
		}
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean active) {
		isActive = active;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}