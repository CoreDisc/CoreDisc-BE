package com.coredisc.application.service.notification;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.NotificationConverter;
import com.coredisc.common.exception.handler.NotificationHandler;
import com.coredisc.domain.mapping.notificationRead.NotificationRead;
import com.coredisc.domain.mapping.notificationRead.NotificationReadRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.notification.Notification;
import com.coredisc.domain.notification.NotificationRepository;
import com.coredisc.presentation.dto.notification.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final NotificationReadRepository notificationReadRepository;


    @Override
    public void createNotification(NotificationRequestDTO request) {

        // 알림 저장
        Notification notification = notificationRepository.save(NotificationConverter.toSaveNotification(request));

        // 읽음 여부 저장
        NotificationRead notificationRead = NotificationConverter.toNotificationRead(notification, request);

        notificationReadRepository.save(notificationRead);
    }

    @Override
    public void readNotification(Member member, Long notificationId) {
        NotificationRead unread = notificationReadRepository
                .findByNotificationIdAndMember(notificationId, member)
                .orElseThrow(() -> new NotificationHandler(ErrorStatus.NOTIFICATION_NOT_FOUND));

        unread.setIsRead(true);
    }

    @Override
    public void readAllNotifications(Member member) {
        List<NotificationRead> unreadList = notificationReadRepository.findAllByMemberAndIsReadFalse(member);

        for (NotificationRead unread : unreadList) {
            unread.setIsRead(true);
        }
    }
}
