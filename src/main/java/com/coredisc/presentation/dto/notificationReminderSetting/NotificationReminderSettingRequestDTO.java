package com.coredisc.presentation.dto.notificationReminderSetting;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationReminderSettingRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationReminderSettingUpdateDTO {
        private Boolean dailyReminderEnabled;

        @Min(0) @Max(23)
        private Integer dailyReminderHour;
        @Min(0) @Max(59)
        private Integer dailyReminderMinute;

        private Boolean unansweredReminderEnabled;

        @Min(0) @Max(23)
        private Integer unansweredReminderHour;
        @Min(0) @Max(59)
        private Integer unansweredReminderMinute;
    }
}
