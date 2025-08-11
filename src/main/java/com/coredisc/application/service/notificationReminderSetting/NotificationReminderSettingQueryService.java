package com.coredisc.application.service.notificationReminderSetting;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.notificationReminderSetting.NotificationReminderSettingResponseDTO;

public interface NotificationReminderSettingQueryService {

    NotificationReminderSettingResponseDTO getNotificationReminderSetting(Member member);
}
