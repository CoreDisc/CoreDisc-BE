package com.coredisc.domain;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_template_id", nullable = false)
    private NotificationTemplate notificationTemplate;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private boolean isEnabled = true;

    @Column(name = "send_time")
    private LocalTime sendTime;

}