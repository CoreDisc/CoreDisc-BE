package com.coredisc.application.service.notification;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.notification.NotificationResponseDTO;
import org.springframework.data.domain.Pageable;

public interface NotificationQueryService {

    // 안읽은 알림 존재 여부
    Boolean hasUnreadNotification(Member member);

    // 사용자의 알림 목록 조회
    CursorDTO<NotificationResponseDTO> getNotifications(Member member, Long cursorId, Pageable pageable);
}
