package com.coredisc.domain.mapping.notificationReminderSetting;

import com.coredisc.domain.member.Member;

import java.util.Optional;

public interface NotificationReminderSettingRepository {

    Optional<NotificationReminderSetting> findByMember(Member member);

    NotificationReminderSetting save(NotificationReminderSetting notificationReminderSetting);
}
