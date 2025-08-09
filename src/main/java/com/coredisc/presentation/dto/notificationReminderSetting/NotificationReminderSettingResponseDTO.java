package com.coredisc.presentation.dto.notificationReminderSetting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationReminderSettingResponseDTO {
    private boolean dailyReminderEnabled;
    private boolean unansweredReminderEnabled;

    private int dailyReminderHour;
    private int dailyReminderMinute;

    private int unansweredReminderHour;
    private int unansweredReminderMinute;
}