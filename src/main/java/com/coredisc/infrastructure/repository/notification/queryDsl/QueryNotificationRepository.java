package com.coredisc.infrastructure.repository.notification.queryDsl;

import com.coredisc.domain.mapping.notificationRead.NotificationRead;
import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryNotificationRepository {

    List<NotificationRead> findNotificationReads(Member member, Long cursorId, Pageable pageable);
}
