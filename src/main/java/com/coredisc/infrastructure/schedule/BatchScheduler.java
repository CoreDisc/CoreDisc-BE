package com.coredisc.infrastructure.schedule;

import com.coredisc.application.service.disc.DiscBatchService;
import com.coredisc.application.service.reportStat.ReportStatBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final DiscBatchService discBatchService;
    private final ReportStatBatchService reportStatBatchService;

    // 매일 자정 (00:00:00)
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void processDailyStatistics() {
        LocalDate targetDate = LocalDate.now().minusDays(1); // 전날 기준
        log.info("🔄 [배치] {}일자 통계 데이터 생성 시작", targetDate);
        runDailyBatch(targetDate);
        log.info("✅ [배치] {}일자 통계 데이터 생성 완료", targetDate);
    }

    // 매월 1일 00:00:00
    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Seoul")
    public void generateMonthlyDiscs() {
        LocalDate now = LocalDate.now();
        LocalDate targetMonth = now.minusMonths(1);
        log.info("📀 [배치] {}년 {}월 디스크 생성 시작", targetMonth.getYear(), targetMonth.getMonthValue());
        discBatchService.generateDiscsForMonth(targetMonth);
        log.info("✅ [배치] {}년 {}월 디스크 생성 완료", targetMonth.getYear(), targetMonth.getMonthValue());
    }

    // 테스트용: 특정 월 기준 디스크 배치 수동 실행
    public void runBatchForMonth(LocalDate targetMonth) {
        log.info("테스트용 배치 실행: {}년 {}월", targetMonth.getYear(), targetMonth.getMonthValue());
        discBatchService.generateDiscsForMonth(targetMonth);
    }

    // ✅ 테스트용: 특정 일자 기준 데일리 통계 실행
    public void runBatchForDay(LocalDate targetDay) {
        log.info("🧪 [테스트] {}일자 통계 배치 실행 시작", targetDay);
        runDailyBatch(targetDay);
        log.info("🧪 [테스트] {}일자 통계 배치 실행 완료", targetDay);
    }

    private void runDailyBatch(LocalDate targetDate) {
        reportStatBatchService.generateDailyStatistics(targetDate); // 답변 시간 저장
        reportStatBatchService.generateMonthlyFixedQuestionStats(targetDate); // 고정 질문 저장
        reportStatBatchService.generateRandomQuestionsStats(targetDate); // 랜덤 질문 저장

        // TODO: 완성되면 추후에 추가하기
        // reportStatBatchService.generateMonthlySelectionDiaryStats(targetDate);
    }
}