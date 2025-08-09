package com.coredisc.common.converter;

import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSetting;
import com.coredisc.presentation.dto.notificationReminderSetting.NotificationReminderSettingResponseDTO;

public class NotificationReminderSettingConverter {

    public static NotificationReminderSettingResponseDTO toResponseDTO(NotificationReminderSetting notificationReminderSetting) {

        return new NotificationReminderSettingResponseDTO(
                notificationReminderSetting.isDailyReminderEnabled(),
                notificationReminderSetting.isUnansweredReminderEnabled(),
                notificationReminderSetting.getDailyReminderTime().getHour(),
                notificationReminderSetting.getDailyReminderTime().getMinute(),
                notificationReminderSetting.getUnansweredReminderTime().getHour(),
                notificationReminderSetting.getUnansweredReminderTime().getMinute()
        );
    }

    // 기본값으로 1번, 2번 알림은 켜두기 (true)
    // 기본값으로 1번은 8시, 2번은 18시로 설정
    // TODO: 날짜 변경 가능성 있음
    public static NotificationReminderSettingResponseDTO defaultResponse() {

        return new NotificationReminderSettingResponseDTO(
                true,
                true,
                8,
                0,
                18,
                0
        );
    }
}
