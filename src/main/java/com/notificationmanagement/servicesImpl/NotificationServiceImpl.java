package com.notificationmanagement.servicesImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.notificationmanagement.kafkadtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.notificationmanagement.config.NotificationSpecification;
import com.notificationmanagement.dtos.NotificationPatchRequest;
import com.notificationmanagement.dtos.NotificationRequest;
import com.notificationmanagement.dtos.NotificationResponse;
import com.notificationmanagement.exception.BadRequestException;
import com.notificationmanagement.exception.ResourceNotFoundException;
import com.notificationmanagement.models.Notification;
import com.notificationmanagement.models.NotificationStatus;
import com.notificationmanagement.models.NotificationTemplate;
import com.notificationmanagement.models.NotificationType;
import com.notificationmanagement.repositories.NotificationRepository;
import com.notificationmanagement.repositories.NotificationTemplateRepository;
import com.notificationmanagement.services.NotificationService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final NotificationTemplateRepository templateRepository;

	public NotificationServiceImpl(NotificationRepository notificationRepository,
			NotificationTemplateRepository templateRepository) {
		this.notificationRepository = notificationRepository;
		this.templateRepository = templateRepository;
	}

	@Autowired
	private StreamBridge streamBridge;
	private Map<Long, ProductEvent> requestMap = new HashMap<>();
	private Map<Long, CategoryEvent> requestMap1 = new HashMap<>();

	@Override
	public NotificationResponse sendNotification(NotificationRequest request) {

		String subject = request.getSubject();
		String message = request.getMessage();

		// If template is provided
		if (request.getTemplateId() != null) {

			NotificationTemplate template = templateRepository.findById(request.getTemplateId())
					.orElseThrow(() -> new ResourceNotFoundException("Template not found"));

			if (!template.getIsActive()) {
				throw new BadRequestException("Template is inactive");
			}

			subject = template.getSubject();
			message = template.getBody();

			// Simple placeholder replacement (example)
			message = processTemplate(message, request.getUserId());
		}

		// Validation
		if (message == null || message.isBlank()) {
			throw new BadRequestException("Message cannot be empty");
		}

		Notification notification = new Notification();
		notification.setUserId(request.getUserId());
		notification.setOrderId(request.getOrderId());
		notification.setPaymentId(request.getPaymentId());
		notification.setShipmentId(request.getShipmentId());
		notification.setNotificationType(request.getNotificationType());
		notification.setSubject(subject);
		notification.setMessage(message);
		notification.setStatus(NotificationStatus.PENDING);
		notification.setCreatedAt(LocalDateTime.now());
		notification.setUpdatedAt(LocalDateTime.now());

		// Simulate sending
		try {
			send(notification);

			notification.setStatus(NotificationStatus.SENT);
			notification.setSentAt(LocalDateTime.now());

		} catch (Exception e) {
			notification.setStatus(NotificationStatus.FAILED);
			notification.setRetryCount(1);
		}

		Notification saved = notificationRepository.save(notification);

		return new NotificationResponse(saved.getId(), saved.getUserId(), saved.getMessage(), saved.getSubject(),
				saved.getStatus().name(), saved.getSentAt());
	}

	private void send(Notification notification) {
		// Simulate sending logic (email/sms/push)
		System.out.println("Sending " + notification.getNotificationType() + " to user " + notification.getUserId());
	}

	@Override
	public NotificationResponse resendNotification(Long id) {

		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

//		// Allow retry only if FAILED
//		if (notification.getStatus() != NotificationStatus.FAILED) {
//			throw new BadRequestException("Only FAILED notifications can be retried");
//		}

		// Increment retry count
		int retryCount = notification.getRetryCount() == null ? 0 : notification.getRetryCount();
		notification.setRetryCount(retryCount + 1);

		notification.setUpdatedAt(LocalDateTime.now());

		try {
			// Simulate sending again
			send(notification);

			notification.setStatus(NotificationStatus.SENT);
			notification.setSentAt(LocalDateTime.now());

		} catch (Exception e) {
			notification.setStatus(NotificationStatus.FAILED);
		}

		Notification updated = notificationRepository.save(notification);

		return new NotificationResponse(updated.getId(), updated.getUserId(), updated.getMessage(),
				updated.getSubject(), updated.getStatus().name(), updated.getSentAt());
	}

	// Simple placeholder replacement
	private String processTemplate(String body, Long userId) {
		return body.replace("{userId}", String.valueOf(userId));
	}

	@Override
	public NotificationResponse updateNotification(Long id, NotificationPatchRequest request) {

		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

		boolean updated = false;

		// Update status
		if (request.getStatus() != null) {
			notification.setStatus(request.getStatus());

			// Set sentAt if status is SENT
			if (request.getStatus() == NotificationStatus.SENT) {
				notification.setSentAt(LocalDateTime.now());
			}

			updated = true;
		}

		// Update message
		if (request.getMessage() != null) {
			if (request.getMessage().isBlank()) {
				throw new BadRequestException("Message cannot be empty");
			}
			notification.setMessage(request.getMessage());
			updated = true;
		}

		// Update subject
		if (request.getSubject() != null) {
			notification.setSubject(request.getSubject());
			updated = true;
		}

		if (!updated) {
			throw new BadRequestException("No valid fields provided for update");
		}

		notification.setUpdatedAt(LocalDateTime.now());

		Notification updatedNotification = notificationRepository.save(notification);

		return new NotificationResponse(updatedNotification.getId(), updatedNotification.getUserId(),
				updatedNotification.getMessage(), updatedNotification.getSubject(),
				updatedNotification.getStatus().name(), updatedNotification.getSentAt());
	}

	@Override
	public Page<NotificationResponse> getNotifications(Long userId, Long orderId, Long paymentId, Long shipmentId,
			NotificationStatus status, NotificationType type, LocalDateTime startDate, LocalDateTime endDate,
			Pageable pageable) {

		var spec = NotificationSpecification.filter(userId, orderId, paymentId, shipmentId, status, type, startDate,
				endDate);

		return notificationRepository.findAll(spec, pageable).map(this::mapToResponse);
	}

	@Override
	public NotificationResponse getById(Long id) {

		Notification notification = notificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

		return mapToResponse(notification);
	}

	private NotificationResponse mapToResponse(Notification n) {
		return new NotificationResponse(n.getId(), n.getUserId(), n.getMessage(), n.getSubject(), n.getStatus().name(),
				n.getSentAt());
	}

	@Override
	public void handleProductEvent(ProductEvent productEvent) {
		System.out.println("HELLOOO---------------");
		Long requestId = ThreadLocalRandom.current().nextLong();

		// Save product info
		requestMap.put(requestId, productEvent);

		// Send request to User Service
		UserFetchRequestEvent request = new UserFetchRequestEvent();
		request.setRequestId(requestId);

		streamBridge.send("userFetch-out-0", request);
		System.out.println("📤 Sent to userFetch-out-0, requestId:------------------------------------ ");
	}

	@Override
	public void handleCategoryEvent(CategoryEvent categoryEvent) {
		System.out.println("HELLOOO---------------");
		Long requestId = ThreadLocalRandom.current().nextLong();

		// Save product info
		requestMap1.put(requestId, categoryEvent);

		// Send request to User Service
		UserFetchRequestEvent request = new UserFetchRequestEvent();
		request.setRequestId(requestId);

		streamBridge.send("userFetch-out-0", request);
		System.out.println("📤 Sent to userFetch-out-0, requestId:------------------------------------ ");
	}


	@Override
	public void handleUserEmails(UserEmailEvent event) {

		ProductEvent product = requestMap.get(event.getRequestId());
		CategoryEvent category=requestMap1.get(event.getRequestId());
		if (product == null && category==null) {
			return;
		}

		for (UserEmailDto user : event.getUsers()) {

			try {
				// 🔥 Send Email
				sendEmail1(user.getUserId(),user.getEmail(),category);
				sendEmail(user.getUserId(), user.getEmail(), product);

			} catch (Exception e) {

				throw new RuntimeException(e);
			}
		}

		requestMap.remove(event.getRequestId());
	}

	private void sendEmail(Long userId, String email, ProductEvent product) {

		// 1️⃣ Create notification object
		Notification notification = new Notification(userId, NotificationType.EMAIL,
				"New Product: " + product.getTitle(), "New Product Added!\n\n" + "Title: " + product.getTitle() + "\n"
						+ "Author: " + product.getAuthor() + "\n" + "Price: ₹" + product.getPrice());

		notificationRepository.save(notification); // Save as PENDING

		// 5️⃣ Update notification status
		notificationRepository.save(notification);
	}

	private void sendEmail1(Long userId, String email, CategoryEvent category) {

		// 1️⃣ Create notification object
		Notification notification = new Notification(userId, NotificationType.EMAIL,
				"New Category: " + category.getCategoryName(), "New Category Added!\n\n"+"Discription is: "+category.getDescription());

		notificationRepository.save(notification); // Save as PENDING

		// 5️⃣ Update notification status
		notificationRepository.save(notification);
	}

}