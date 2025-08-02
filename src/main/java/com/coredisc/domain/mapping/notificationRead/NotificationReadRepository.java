package com.coredisc.domain.mapping.notificationRead;

import com.coredisc.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface NotificationReadRepository {

    NotificationRead save(NotificationRead notificationRead);

    boolean existsUnreadByMember(Member member);

    Optional<NotificationRead> findByNotificationIdAndMember(Long notificationId, Member member);

    List<NotificationRead> findAllByMemberAndIsReadFalse(Member member);
}
