package com.coredisc.application.service.notification;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.notification.NotificationRequestDTO;

public interface NotificationCommandService {

    // 알림 생성
    void createNotification(NotificationRequestDTO request);

    // 개별 읽음 처리
    void readNotification(Member member, Long notificationId);

    // 전체 읽음 처리
    void readAllNotifications(Member member);
}
