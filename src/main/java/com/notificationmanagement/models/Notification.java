package com.notificationmanagement.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications",
		indexes = {
				@Index(name = "idx_user_id", columnList = "userId"),
				@Index(name = "idx_status", columnList = "status"),
				@Index(name = "idx_type", columnList = "notificationType")
		}
)
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Required
	@NotNull(message = "User ID is required")
	@Column(nullable = false)
	private Long userId;

	// Optional references
	@Column
	private Long orderId;

	@Column
	private Long paymentId;

	@Column
	private Long shipmentId;

	// Notification Type
	@NotNull(message = "Notification type is required")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType notificationType;

	// Optional subject (mainly for EMAIL/PUSH)
	@Column(length = 255)
	private String subject;

	// Message content
	@NotBlank(message = "Message content is required")
	@Column(nullable = false, columnDefinition = "TEXT")
	private String message;

	// Status
	@NotNull(message = "Status is required")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationStatus status = NotificationStatus.PENDING;

	// When notification was sent
	private LocalDateTime sentAt;

	// Retry count
	@Column(nullable = false)
	private Integer retryCount = 0;

	// Timestamps
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	// Constructors
	public Notification() {}

	public Notification(Long userId, NotificationType notificationType, String subject, String message) {
		this.userId = userId;
		this.notificationType = notificationType;
		this.subject = subject;
		this.message = message;
		this.status = NotificationStatus.PENDING;
		this.retryCount = 0;
	}

	// Lifecycle methods
	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;

		validateNotification();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();

		validateNotification();
	}

	// Business validation
	private void validateNotification() {
		if (this.notificationType == NotificationType.EMAIL &&
				(this.subject == null || this.subject.isBlank())) {
			throw new IllegalArgumentException("Subject is required for EMAIL notifications");
		}
	}

	// Helper methods
	public void markAsSent() {
		this.status = NotificationStatus.SENT;
		this.sentAt = LocalDateTime.now();
	}

	public void markAsFailed() {
		this.status = NotificationStatus.FAILED;
		this.retryCount++;
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public Long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
