package com.coredisc.domain.mapping.notificationReminderSetting;

import com.coredisc.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationReminderSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private boolean dailyReminderEnabled;
    private boolean unansweredReminderEnabled;

    private LocalTime dailyReminderTime;
    private LocalTime unansweredReminderTime;

    public void updateDailyReminderEnabled(boolean enabled) { this.dailyReminderEnabled = enabled; }
    public void updateUnansweredReminderEnabled(boolean enabled) { this.unansweredReminderEnabled = enabled; }
    public void changeDailyReminderTime(int hour, int minute) { this.dailyReminderTime = LocalTime.of(hour, minute); }
    public void changeUnansweredReminderTime(int hour, int minute) { this.unansweredReminderTime = LocalTime.of(hour, minute); }

}