package com.coredisc.infrastructure.repository.notification;

import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSetting;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaNotificationReminderSettingRepository extends JpaRepository<NotificationReminderSetting, Long> {

    Optional<NotificationReminderSetting> findByMember(Member member);
}
