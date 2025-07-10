package com.coredisc.domain;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.NotificationFrequency;
import com.coredisc.domain.common.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "notification_frequency", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationFrequency notificationFrequency;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "notificationTemplate", cascade = CascadeType.ALL)
    private List<NotificationSetting> notificationSettings;

    @OneToMany(mappedBy = "notificationTemplate", cascade = CascadeType.ALL)
    private List<MemberNotification> memberNotifications;

}
