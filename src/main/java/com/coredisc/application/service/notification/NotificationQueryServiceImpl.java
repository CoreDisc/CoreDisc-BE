package com.coredisc.application.service.notification;

import com.coredisc.common.converter.NotificationConverter;
import com.coredisc.domain.mapping.notificationRead.NotificationRead;
import com.coredisc.domain.mapping.notificationRead.NotificationReadRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.infrastructure.repository.notification.queryDsl.QueryNotificationRepository;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.notification.NotificationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationReadRepository notificationReadRepository;
    private final QueryNotificationRepository queryNotificationRepository;

    // 안읽은 알림 존재 여부
    @Override
    public Boolean hasUnreadNotification(Member member) {

        return notificationReadRepository.existsUnreadByMember(member);
    }

    // 사용자의 알림 목록 조회
    @Override
    public CursorDTO<NotificationResponseDTO> getNotifications(Member member, Long cursorId, Pageable pageable) {

        List<NotificationRead> result = queryNotificationRepository.findNotificationReads(member, cursorId, pageable);

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) result.remove(pageable.getPageSize());

        List<NotificationResponseDTO> dtos = result.stream()
                .map(NotificationConverter::toNotificationDTO)
                .toList();

        return new CursorDTO<>(dtos, hasNext);
    }
}
