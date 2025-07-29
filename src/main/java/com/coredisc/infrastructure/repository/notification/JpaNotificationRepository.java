package com.coredisc.infrastructure.repository.notification;

import com.coredisc.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationRepository extends JpaRepository<Notification, Long> {

}
