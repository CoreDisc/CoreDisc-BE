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
}