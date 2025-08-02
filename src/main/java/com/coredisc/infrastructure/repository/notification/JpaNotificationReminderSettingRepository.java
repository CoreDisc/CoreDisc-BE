package com.coredisc.infrastructure.repository.notification;

import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationReminderSettingRepository extends JpaRepository<NotificationReminderSetting, Long> {

}
