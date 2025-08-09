package com.coredisc.common.converter;

import com.coredisc.domain.mapping.notificationRead.NotificationRead;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.notification.Notification;
import com.coredisc.presentation.dto.notification.NotificationRequestDTO;
import com.coredisc.presentation.dto.notification.NotificationResponseDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

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

    public static Notification toSaveNotification(NotificationRequestDTO request) {

        return Notification.builder()
                .sender(request.sender())
                .receiver(request.receiver())
                .type(request.type())
                .targetId(request.targetId())
                .content(request.content())
                .build();
    }

    public static NotificationRead toNotificationRead(Notification notification, NotificationRequestDTO request) {

        return NotificationRead.builder()
                .notification(notification)
                .member(request.receiver())
                .isRead(false)
                .build();
    }

    public static String createdAtToTimestamp(LocalDateTime createdAt) {

        Period period = Period.between(createdAt.toLocalDate(), LocalDateTime.now().toLocalDate());
        long years = period.getYears();
        long months = period.getMonths();
        long days = period.getDays();

        Duration duration = Duration.between(createdAt, LocalDateTime.now());
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds();

        if (years > 0) {
            return years + "년 전";
        } else if (months > 0) {
            return months + "개월 전";
        } else if (days > 0) {
            return days + "일 전";
        } else if (hours > 0) {
            return hours + "시간 전";
        } else if (minutes > 0) {
            return minutes + "분 전";
        } else {
            return seconds + "초 전";
        }
    }
}
