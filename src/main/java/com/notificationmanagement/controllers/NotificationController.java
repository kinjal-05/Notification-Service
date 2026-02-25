package com.notificationmanagement.controllers;

import com.notificationmanagement.dtos.NotificationPatchRequest;
import com.notificationmanagement.dtos.NotificationRequest;
import com.notificationmanagement.dtos.NotificationResponse;
import com.notificationmanagement.models.NotificationStatus;
import com.notificationmanagement.models.NotificationType;
import com.notificationmanagement.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@PostMapping
	public ResponseEntity<NotificationResponse> sendNotification(
			@Valid @RequestBody NotificationRequest request) {

		NotificationResponse response = notificationService.sendNotification(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{id}/resend")
	public ResponseEntity<NotificationResponse> resendNotification(@PathVariable Long id) {

		NotificationResponse response = notificationService.resendNotification(id);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<NotificationResponse> updateNotification(
			@PathVariable Long id,
			@RequestBody NotificationPatchRequest request) {

		NotificationResponse response = notificationService.updateNotification(id, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<Page<NotificationResponse>> getNotifications(

			@RequestParam(required = false) Long userId,
			@RequestParam(required = false) Long orderId,
			@RequestParam(required = false) Long paymentId,
			@RequestParam(required = false) Long shipmentId,

			@RequestParam(required = false) NotificationStatus status,
			@RequestParam(required = false) NotificationType type,

			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
			LocalDateTime startDate,

			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
			LocalDateTime endDate,

			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt,desc") String[] sort
	) {

		Sort sorting = Sort.by(
				Sort.Direction.fromString(sort[1]),
				sort[0]
		);

		Pageable pageable = PageRequest.of(page, size, sorting);

		Page<NotificationResponse> result = notificationService.getNotifications(
				userId, orderId, paymentId, shipmentId,
				status, type, startDate, endDate, pageable
		);

		return ResponseEntity.ok(result);
	}

	// ✅ Get by ID
	@GetMapping("/{id}")
	public ResponseEntity<NotificationResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(notificationService.getById(id));
	}


}