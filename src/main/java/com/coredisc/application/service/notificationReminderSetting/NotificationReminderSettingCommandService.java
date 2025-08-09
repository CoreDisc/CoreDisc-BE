package com.coredisc.application.service.notificationReminderSetting;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.notificationReminderSetting.NotificationReminderSettingRequestDTO;
import com.coredisc.presentation.dto.notificationReminderSetting.NotificationReminderSettingResponseDTO;

public interface NotificationReminderSettingCommandService {

    void defaultNotificationReminderSetting(Member member);

    NotificationReminderSettingResponseDTO updateNotificationReminderSetting(Member member, NotificationReminderSettingRequestDTO.NotificationReminderSettingUpdateDTO request);
}
