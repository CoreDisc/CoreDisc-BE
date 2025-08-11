package com.coredisc.infrastructure.repository.notification;

import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSetting;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaNotificationReminderSettingRepository extends JpaRepository<NotificationReminderSetting, Long> {

    Optional<NotificationReminderSetting> findByMember(Member member);

    @Query("""
        SELECT n
        FROM NotificationReminderSetting n
        WHERE n.dailyReminderEnabled = true
          AND FUNCTION('HOUR', n.dailyReminderTime) = :hour
          AND FUNCTION('MINUTE', n.dailyReminderTime) = :minute
    """)
    List<NotificationReminderSetting> findAllByDailyReminderEnabledTrueAndDailyReminderTime(
            @Param("hour") int hour,
            @Param("minute") int minute
    );

    @Query("""
        SELECT n
        FROM NotificationReminderSetting n
        WHERE n.unansweredReminderEnabled = true
          AND FUNCTION('HOUR', n.unansweredReminderTime) = :hour
          AND FUNCTION('MINUTE', n.unansweredReminderTime) = :minute
    """)
    List<NotificationReminderSetting> findAllByUnansweredReminderEnabledTrueAndDailyReminderTime(
            @Param("hour") int hour,
            @Param("minute") int minute
    );
}
