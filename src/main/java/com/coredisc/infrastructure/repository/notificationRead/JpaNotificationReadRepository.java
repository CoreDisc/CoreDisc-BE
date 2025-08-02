package com.coredisc.infrastructure.repository.notificationRead;

import com.coredisc.domain.mapping.notificationRead.NotificationRead;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaNotificationReadRepository extends JpaRepository<NotificationRead, Long> {

    boolean existsByMemberAndIsReadFalse(Member member);

    Optional<NotificationRead> findByNotificationIdAndMember(Long notificationId, Member member);

    List<NotificationRead> findAllByMemberAndIsReadFalse(Member member);
}
