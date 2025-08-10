package com.coredisc.application.schedule;

import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSetting;
import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSettingRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.postAnswer.PostAnswerRepository;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.infrastructure.repository.question.JpaTodayQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class NotificationReminderScheduler {

    private final NotificationReminderSettingRepository notificationReminderSettingRepository;
    private final JpaTodayQuestionRepository todayQuestionRepository;
    private final PostAnswerRepository postAnswerRepository;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void reminderNotification() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        int hh = LocalTime.now(ZoneId.of("Asia/Seoul")).getHour();
        int mm = LocalTime.now(ZoneId.of("Asia/Seoul")).getMinute();

        /*
        데일리 리마인더 알림 생성 (사용자가 설정한 시간에 생성되도록 시간, 분 매칭)
        - 고정질문 < 4일 때 => 질문 생성해보세요 알림
        - 답변 작성 X => 답변 작성해보세요 알림
        - 오늘 답변 전부 마무리한 상태 => 알림 발송 X
        */
        List<NotificationReminderSetting> dailyTargets =
                notificationReminderSettingRepository
                        .findAllByDailyReminderEnabledTrueAndDailyReminderTime(hh, mm);
        dailyTargets.forEach(setting -> processDailyReminder(setting.getMember(), today));


        /*
        미응답 리마인더 알림 생성 (사용자가 설정한 시간에 생성되도록 시간, 분 매칭)
        - 고정질문 < 4일 때 => 질문 생성해보세요 알림
        - 답변 작성 X => 답변 작성해보세요 알림
        - 오늘 답변 전부 마무리한 상태 => 알림 발송 X
        */
        List<NotificationReminderSetting> unansweredTargets =
                notificationReminderSettingRepository.findAllByUnansweredReminderEnabledTrueAndDailyReminderTime(hh, mm);
        unansweredTargets.forEach(notificationReminderSetting -> processUnansweredReminder(notificationReminderSetting.getMember(), today));
    }

    private void processDailyReminder(Member member, LocalDate today) {
        if (hasTodayQuestions(member, today)) {
            // TODO: FCM - 질문 생성 알림
            log.info("DAILY_REMINDER - memberId={} : 오늘의 질문을 생성해 주세요.", member.getId());
        } else if (hasUnansweredQuestions(member, today)) {
            //TODO: FCM - 답변 작성 알림
            log.info("DAILY_REMINDER - memberId={} : 오늘의 질문 답변을 작성해 주세요.", member.getId());
        } else {
            log.info("DAILY_REMINDER - memberId={} : 모든 질문 답변 완료했으니까 알림 미발송", member.getId());
        }
    }

    private void processUnansweredReminder(Member member, LocalDate today) {
        if (hasTodayQuestions(member, today)) {
            // TODO: FCM - 질문 생성 알림
            log.info("UNANSWERED_QUESTION - memberId={} : 오늘의 질문을 생성해 주세요.", member.getId());
        } else if (hasUnansweredQuestions(member, today)) {
            //TODO: FCM - 답변 작성 알림
            log.info("UNANSWERED_QUESTION - memberId={} : 오늘의 질문 답변을 마저 작성해 주세요.", member.getId());
        } else {
            log.info("UNANSWERED_QUESTION - memberId={} : 모든 질문 답변 완료했으니까 알림 미발송", member.getId());
        }
    }

    // 오늘의 질문 중 하나라도 없는지 체크
    private boolean hasTodayQuestions(Member member, LocalDate today) {
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        for (int order = 1; order <= 4; order++) {
            Optional<TodayQuestion> todayQuestion = (order == 4)
                    ? todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDate(member, order, today)
                    : todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(member, order, startOfMonth, endOfMonth);

            // 질문이 없거나, 내용이 비어있으면 true
            if (todayQuestion.isEmpty() || todayQuestion.get().getQuestionContent() == null || todayQuestion.get().getQuestionContent().isBlank()) {
                return true;
            }
        }
        return false; // 4개 다 있음
    }

    // 오늘의 질문에 대한 답변 하나라도 없는지 체크
    private boolean hasUnansweredQuestions(Member member, LocalDate today) {
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        for (int order = 1; order <= 4; order++) {
            Optional<TodayQuestion> todayQuestion = (order == 4)
                    ? todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDate(member, order, today)
                    : todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(member, order, startOfMonth, endOfMonth);

            if (todayQuestion.isEmpty()) {
                return true; // 질문이 없으면 답변도 없음
            }

            // 질문별 답변 존재 여부 확인
            if (! postAnswerRepository.existsByPostMemberAndAnswerOrderAndPostCreatedAtBetween(member, order, startOfDay, endOfDay)) {
                return true; // 번호에 해당되는 답변이 없음
            }

        }

        return false; // 전부 답변 있음
    }

}
