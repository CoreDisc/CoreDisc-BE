package com.coredisc.infrastructure.repository.notification;

import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSetting;
import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSettingRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationReminderSettingRepositoryAdaptor implements NotificationReminderSettingRepository {

    private final JpaNotificationReminderSettingRepository jpaNotificationReminderSettingRepository;

    @Override
    public Optional<NotificationReminderSetting> findByMember(Member member) {
        return jpaNotificationReminderSettingRepository.findByMember(member);
    }

    @Override
    public NotificationReminderSetting save(NotificationReminderSetting notificationReminderSetting) {
        return jpaNotificationReminderSettingRepository.save(notificationReminderSetting);
    }

    @Override
    public List<NotificationReminderSetting> findAllByDailyReminderEnabledTrueAndDailyReminderTime(int hh, int mm) {
        return jpaNotificationReminderSettingRepository.findAllByDailyReminderEnabledTrueAndDailyReminderTime(hh, mm);
    }

    @Override
    public List<NotificationReminderSetting> findAllByUnansweredReminderEnabledTrueAndDailyReminderTime(int hh, int mm) {
        return jpaNotificationReminderSettingRepository.findAllByUnansweredReminderEnabledTrueAndDailyReminderTime(hh, mm);
    }
}
