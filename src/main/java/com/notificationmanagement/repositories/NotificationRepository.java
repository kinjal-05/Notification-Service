package com.notificationmanagement.repositories;

import com.notificationmanagement.models.Notification;
import com.notificationmanagement.models.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
	Page<Notification> findByStatus(NotificationStatus status, Pageable pageable);

}