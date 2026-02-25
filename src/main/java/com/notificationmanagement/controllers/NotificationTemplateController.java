package com.notificationmanagement.controllers;

import com.notificationmanagement.dtos.*;
import com.notificationmanagement.models.NotificationTemplate;
import com.notificationmanagement.models.NotificationType;
import com.notificationmanagement.services.NotificationTemplateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification-templates")
public class NotificationTemplateController {

	private final NotificationTemplateService service;

	public NotificationTemplateController(NotificationTemplateService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<NotificationTemplate> createTemplate(
			@Valid @RequestBody NotificationTemplateRequest request) {

		NotificationTemplate response = service.createTemplate(request);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<NotificationTemplate> updateTemplate(
			@PathVariable Long id,
			@Valid @RequestBody NotificationTemplateUpdateRequest request) {

		NotificationTemplate updated = service.updateTemplate(id, request);

		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteTemplate(@PathVariable Long id) {

		service.deleteTemplate(id);

		ApiResponse response = new ApiResponse(
				HttpStatus.OK.value(),
				"Notification template deleted successfully"
		);

		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse1<PageResponse<NotificationTemplate>>> getAllTemplates(

			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir,

			@RequestParam(required = false) Boolean isActive,
			@RequestParam(required = false) NotificationType type,
			@RequestParam(required = false) String name
	) {

		PageResponse<NotificationTemplate> data = service.getAll(
				page, size, sortBy, sortDir, isActive, type, name
		);

		return ResponseEntity.ok(
				new ApiResponse1<>(200, "Templates fetched successfully", data)
		);
	}
}