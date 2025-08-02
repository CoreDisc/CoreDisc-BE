package com.coredisc.infrastructure.repository.notification.queryDsl;

import com.coredisc.domain.mapping.notificationRead.NotificationRead;
import com.coredisc.domain.mapping.notificationRead.QNotificationRead;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.QMember;
import com.coredisc.domain.notification.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryNotificationRepositoryImpl implements QueryNotificationRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NotificationRead> findNotificationReads(Member member, Long cursorId, Pageable pageable) {
        QNotificationRead notificationRead = QNotificationRead.notificationRead;

        QNotification notification = QNotification.notification;
        QMember sender = QMember.member;

        return queryFactory
                .selectFrom(notificationRead)
                .leftJoin(notificationRead.notification, notification).fetchJoin()
                .leftJoin(notification.sender, sender).fetchJoin()
                .where(
                        notificationRead.member.eq(member),
                        cursorId != null ? notification.id.lt(cursorId) : null
                )
                .orderBy(notification.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }
}
