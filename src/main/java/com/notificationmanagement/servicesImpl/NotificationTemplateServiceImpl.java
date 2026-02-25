package com.notificationmanagement.servicesImpl;

import com.notificationmanagement.config.NotificationTemplateSpecification;
import com.notificationmanagement.dtos.NotificationTemplateRequest;
import com.notificationmanagement.dtos.NotificationTemplateUpdateRequest;
import com.notificationmanagement.dtos.PageResponse;
import com.notificationmanagement.exception.ResourceAlreadyExistsException;
import com.notificationmanagement.exception.ResourceNotFoundException;
import com.notificationmanagement.models.NotificationTemplate;
import com.notificationmanagement.models.NotificationType;
import com.notificationmanagement.repositories.NotificationTemplateRepository;
import com.notificationmanagement.services.NotificationTemplateService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

	private final NotificationTemplateRepository repository;

	public NotificationTemplateServiceImpl(NotificationTemplateRepository repository) {
		this.repository = repository;
	}

	@Override
	public NotificationTemplate createTemplate(NotificationTemplateRequest request) {

		// Check duplicate name
		if (repository.existsByName(request.getName())) {
			throw new ResourceAlreadyExistsException("Template already exists with name: " + request.getName());
		}

		// Business validation
		if (request.getType() == NotificationType.EMAIL &&
				(request.getSubject() == null || request.getSubject().isBlank())) {
			throw new IllegalArgumentException("Subject is required for EMAIL templates");
		}

		// Map DTO to Entity
		NotificationTemplate template = new NotificationTemplate();
		template.setName(request.getName());
		template.setType(request.getType());
		template.setSubject(request.getSubject());
		template.setBody(request.getBody());
		template.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

		return repository.save(template);
	}

	@Override
	public NotificationTemplate updateTemplate(Long id, NotificationTemplateUpdateRequest request) {

		NotificationTemplate template = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));

		// Update fields only if present (PATCH behavior)

		if (request.getName() != null && !request.getName().equals(template.getName())) {

			if (repository.existsByName(request.getName())) {
				throw new ResourceAlreadyExistsException("Template already exists with name: " + request.getName());
			}

			template.setName(request.getName());
		}

		if (request.getType() != null) {
			template.setType(request.getType());
		}

		if (request.getSubject() != null) {
			template.setSubject(request.getSubject());
		}

		if (request.getBody() != null) {
			template.setBody(request.getBody());
		}

		if (request.getIsActive() != null) {
			template.setIsActive(request.getIsActive());
		}

		// Business validation
		if (template.getType() == NotificationType.EMAIL &&
				(template.getSubject() == null || template.getSubject().isBlank())) {
			throw new IllegalArgumentException("Subject is required for EMAIL templates");
		}

		return repository.save(template);
	}

	@Override
	public void deleteTemplate(Long id) {

		NotificationTemplate template = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));

		repository.delete(template);
	}

	@Override
	public PageResponse<NotificationTemplate> getAll(
			int page,
			int size,
			String sortBy,
			String sortDir,
			Boolean isActive,
			NotificationType type,
			String name
	) {

		Sort sort = sortDir.equalsIgnoreCase("desc")
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Specification<NotificationTemplate> spec =
				NotificationTemplateSpecification.filter(isActive, type, name);

		Page<NotificationTemplate> result = repository.findAll(spec, pageable);

		return new PageResponse<>(
				result.getContent(),
				result.getNumber(),
				result.getSize(),
				result.getTotalElements(),
				result.getTotalPages(),
				result.isLast()
		);
	}
}
