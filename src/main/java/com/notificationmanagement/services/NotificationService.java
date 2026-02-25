package com.notificationmanagement.services;

import java.time.LocalDateTime;

import com.notificationmanagement.kafkadtos.CategoryEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.notificationmanagement.dtos.NotificationPatchRequest;
import com.notificationmanagement.dtos.NotificationRequest;
import com.notificationmanagement.dtos.NotificationResponse;
import com.notificationmanagement.kafkadtos.ProductEvent;
import com.notificationmanagement.kafkadtos.UserEmailEvent;
import com.notificationmanagement.models.NotificationStatus;
import com.notificationmanagement.models.NotificationType;

public interface NotificationService {
	NotificationResponse resendNotification(Long id);

	NotificationResponse sendNotification(NotificationRequest request);

	NotificationResponse updateNotification(Long id, NotificationPatchRequest request);

	Page<NotificationResponse> getNotifications(Long userId, Long orderId, Long paymentId, Long shipmentId,
			NotificationStatus status, NotificationType type, LocalDateTime startDate, LocalDateTime endDate,
			Pageable pageable);

	public void handleProductEvent(ProductEvent productEvent);
	public void handleCategoryEvent(CategoryEvent categoryEvent);
	public void handleUserEmails(UserEmailEvent event);

	NotificationResponse getById(Long id);
}
