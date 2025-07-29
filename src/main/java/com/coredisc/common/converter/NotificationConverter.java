package com.coredisc.common.converter;

import com.coredisc.domain.mapping.notificationRead.NotificationRead;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.notification.Notification;
import com.coredisc.presentation.dto.notification.NotificationResponseDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationConverter {

    public static NotificationResponseDTO toNotificationDTO(NotificationRead notificationRead) {
        Notification notification = notificationRead.getNotification();

        Member sender = notification.getSender();

        return NotificationResponseDTO.builder()
                .notificationId(notification.getId())
                .type(notification.getType())
                .content(notification.getContent())
                .targetId(notification.getTargetId())
                .senderId(sender != null ? sender.getId() : null)
                .senderNickname(sender != null ? sender.getNickname() : null)
                .profileImgDTO(ProfileImgConverter.toProfileImgDTO(notification.getSender().getProfileImg()))
                .isRead(notificationRead.getIsRead())
                .createdAt(notification.getCreatedAt())
                .timeStamp(createdAtToTimestamp(notification.getCreatedAt()))
                .build();
    }

    public static String createdAtToTimestamp(LocalDateTime createdAt) {

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() < 1) {
            return duration.toSeconds() + "초 전";
        } else if (duration.toHours() < 1) {
            return duration.toMinutes() + "분 전";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + "시간 전";
        } else if (duration.toHours() < 48) {
            return "어제 " + createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            return createdAt.format(DateTimeFormatter.ofPattern("MM/dd"));
        }
    }
}
