package com.coredisc.domain.notification;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.NotificationType;
import com.coredisc.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림 받는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    // 알림을 발생시킨 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String content;

    // 어디로 이동할지 (게시글 ID, 유저 ID 등)
    @Column(name = "target_id")
    private Long targetId;
}
