package com.notificationmanagement.config;

import com.notificationmanagement.models.NotificationTemplate;
import com.notificationmanagement.models.NotificationType;
import org.springframework.data.jpa.domain.Specification;

public class NotificationTemplateSpecification {

	public static Specification<NotificationTemplate> filter(
			Boolean isActive,
			NotificationType type,
			String name
	) {
		return (root, query, cb) -> {

			var predicates = cb.conjunction();

			if (isActive != null) {
				predicates.getExpressions().add(cb.equal(root.get("isActive"), isActive));
			}

			if (type != null) {
				predicates.getExpressions().add(cb.equal(root.get("type"), type));
			}

			if (name != null && !name.isBlank()) {
				predicates.getExpressions().add(
						cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%")
				);
			}

			return predicates;
		};
	}
}