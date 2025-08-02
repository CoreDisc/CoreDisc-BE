package com.coredisc.infrastructure.repository.notificationRead;

import com.coredisc.domain.mapping.notificationRead.NotificationRead;
import com.coredisc.domain.mapping.notificationRead.NotificationReadRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationReadRepositoryAdaptor implements NotificationReadRepository {

    private final JpaNotificationReadRepository jpaNotificationReadRepository;

    @Override
    public NotificationRead save(NotificationRead notificationRead) {
        return jpaNotificationReadRepository.save(notificationRead);
    }

    @Override
    public boolean existsUnreadByMember(Member member) {
        return jpaNotificationReadRepository.existsByMemberAndIsReadFalse(member);
    }

    @Override
    public Optional<NotificationRead> findByNotificationIdAndMember(Long notificationId, Member member) {
        return jpaNotificationReadRepository.findByNotificationIdAndMember(notificationId, member);
    }

    @Override
    public List<NotificationRead> findAllByMemberAndIsReadFalse(Member member) {
        return jpaNotificationReadRepository.findAllByMemberAndIsReadFalse(member);
    }
}
