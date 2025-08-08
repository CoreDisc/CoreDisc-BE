package com.coredisc.application.schedule;

import com.coredisc.application.service.disc.DiscBatchService;
import com.coredisc.application.service.post.PostCommandService;
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
    private final PostCommandService postCommandService;

    // 매일 자정 (00:00:00)
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void processDailyStatistics() {
        LocalDate targetDate = LocalDate.now().minusDays(1); // 전날 기준
        log.info("🔄 [배치] {}일자 통계 데이터 생성 시작", targetDate);
        runDailyBatch(targetDate);
        log.info("✅ [배치] {}일자 통계 데이터 생성 완료", targetDate);
        postCommandService.cleanupOldTempPosts(targetDate);
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
        int errorCount = 0;

        try {
            reportStatBatchService.generateDailyStatistics(targetDate);
        } catch (Exception e) {
            errorCount++;
            log.error("[배치] generateDailyStatistics 에러: {}", e.getMessage(), e);
        }

        try {
            reportStatBatchService.generateMonthlyFixedQuestionStats(targetDate);
        } catch (Exception e) {
            errorCount++;
            log.error("[배치] generateMonthlyFixedQuestionStats 에러: {}", e.getMessage(), e);
        }

        try {
            reportStatBatchService.generateRandomQuestionsStats(targetDate);
        } catch (Exception e) {
            errorCount++;
            log.error("[배치] generateRandomQuestionsStats 에러: {}", e.getMessage(), e);
        }

        try {
            reportStatBatchService.generateMonthlySelectionDiaryStats(targetDate);
        } catch (Exception e) {
            errorCount++;
            log.error("[배치] generateMonthlySelectionDiaryStats 에러: {}", e.getMessage(), e);
        }

        if (errorCount > 0) {
            log.warn("[배치] {}일자 통계 배치 작업 완료 - 에러 발생 횟수: {}", targetDate, errorCount);
        } else {
            log.info("[배치] {}일자 통계 배치 작업 완료 - 에러 없이 정상 종료", targetDate);
        }
    }
}