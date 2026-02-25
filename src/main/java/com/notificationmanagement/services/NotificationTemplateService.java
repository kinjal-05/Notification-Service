package com.notificationmanagement.services;

import com.notificationmanagement.dtos.NotificationTemplateRequest;
import com.notificationmanagement.dtos.NotificationTemplateUpdateRequest;
import com.notificationmanagement.dtos.PageResponse;
import com.notificationmanagement.models.NotificationTemplate;
import com.notificationmanagement.models.NotificationType;

public interface NotificationTemplateService {

	NotificationTemplate updateTemplate(Long id, NotificationTemplateUpdateRequest request);
	NotificationTemplate createTemplate(NotificationTemplateRequest request);
	void deleteTemplate(Long id);

	PageResponse<NotificationTemplate> getAll(
			int page,
			int size,
			String sortBy,
			String sortDir,
			Boolean isActive,
			NotificationType type,
			String name
	);
}

