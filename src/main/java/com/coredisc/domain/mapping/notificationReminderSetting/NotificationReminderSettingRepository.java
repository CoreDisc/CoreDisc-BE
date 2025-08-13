package com.coredisc.domain.mapping.notificationReminderSetting;

import com.coredisc.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface NotificationReminderSettingRepository {

    Optional<NotificationReminderSetting> findByMember(Member member);

    NotificationReminderSetting save(NotificationReminderSetting notificationReminderSetting);

    List<NotificationReminderSetting> findAllByDailyReminderEnabledTrueAndDailyReminderTime(int hh, int mm);

    List<NotificationReminderSetting> findAllByUnansweredReminderEnabledTrueAndDailyReminderTime(int hh, int mm);
}
