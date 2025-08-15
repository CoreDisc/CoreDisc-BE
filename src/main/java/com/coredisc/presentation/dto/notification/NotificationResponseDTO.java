package com.coredisc.presentation.dto.notification;

import com.coredisc.domain.common.enums.NotificationType;
import com.coredisc.presentation.dto.profileImg.ProfileImgResponseDTO;
import lombok.Builder;

import java.time.LocalDateTime;

/*
    - 알림 id
    - 알림 타입
    - 알림 내용
    - 타겟id(팔로우, 게시글 등의 id)
    - 보낸 유저 id
    - 보낸 유저의 nickname
    - 보낸 유저의 프로필 이미지
    - 알림 읽음 여부
    - 알림 생성 시간
    - 타임 스탬프
*/

@Builder
public record NotificationResponseDTO (
    Long notificationId,
    NotificationType type,
    String content,
    Long targetId,
    Long senderId,
    String senderNickname,
    String senderUsername,
    ProfileImgResponseDTO.ProfileImgDTO profileImgDTO,
    boolean isRead,
    LocalDateTime createdAt,
    String timeStamp
){}