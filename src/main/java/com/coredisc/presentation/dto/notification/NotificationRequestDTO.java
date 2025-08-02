package com.coredisc.presentation.dto.notification;

import com.coredisc.domain.common.enums.NotificationType;
import com.coredisc.domain.member.Member;

public record NotificationRequestDTO(
        NotificationType type,
        Member sender,
        Member receiver,
        String content,
        Long targetId // 게시글, 질문, 유저 ID 등
) {}

