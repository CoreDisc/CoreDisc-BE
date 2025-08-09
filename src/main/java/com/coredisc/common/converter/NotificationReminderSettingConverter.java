package com.coredisc.common.converter;

import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSetting;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.notificationReminderSetting.NotificationReminderSettingResponseDTO;

import java.time.LocalTime;

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

    public static NotificationReminderSetting toDefault(Member member) {
        return NotificationReminderSetting.builder()
                .member(member)
                .dailyReminderEnabled(true)
                .unansweredReminderEnabled(true)
                .dailyReminderTime(LocalTime.of(8, 0))
                .unansweredReminderTime(LocalTime.of(18, 0))
                .build();
    }
}
