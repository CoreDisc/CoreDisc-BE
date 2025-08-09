package com.coredisc.application.schedule;

import com.coredisc.application.service.notification.NotificationCommandService;
import com.coredisc.domain.common.enums.NotificationType;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.presentation.dto.notification.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class NotificationScheduler {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final NotificationCommandService notificationCommandService;

    // 매일 23시 임시저장 알림
    // 발행되지 않고 임시저장된 게시글이 있을 때, 알림 생성
    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Seoul")
    public void cleanupOldTempPostsNotification() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        log.info("[임시저장 알림 테스트] {} 기준 임시저장 게시글 만료 알림 전송 시작", today);

        // 오늘 작성된 임시저장 게시글이 있는 회원만 조회
        List<Long> memberIds = postRepository.findDistinctMemberIdsByStatusAndCreatedAtBetween(
                PostStatus.TEMP, startOfDay, endOfDay
        );

        if (memberIds.isEmpty()) {
            log.info("[임시저장 알림 테스트] 오늘 임시저장 게시글이 없어 알림 전송을 건너뜁니다.");
            return;
        }

        // 유저마다 1번만 보내기
        Set<Long> receiverIds = new HashSet<>(memberIds);
        List<Member> receivers = memberRepository.findAllById(receiverIds);

        int sent = 0;
        for (Member receiver : receivers) {
            notificationCommandService.createNotification(
                    new NotificationRequestDTO(
                            NotificationType.TEMP_POSTS, // 알림 타입(임시저장 게시글)
                            receiver, // 작성자의 프로필 이미지를 사용할 것이기에 알림 sender를 작성자로 함
                            receiver, // receiver
                            "작성 중인 Core Disc 가 곧 사라져요.",
                            null // 게시글 작성 화면으로 이동해야 하기에 null로 작성 (targetId 없음)
                    )
            );
            sent++;
        }
        log.info("[임시저장 알림테스트] 임시저장 게시글 만료 알림 전송 완료 - 총 {}명", sent);
    }
}
