package com.notificationmanagement.repositories;

import com.notificationmanagement.models.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long>, JpaSpecificationExecutor<NotificationTemplate> {

	Optional<NotificationTemplate> findByName(String name);

	boolean existsByName(String name);
}