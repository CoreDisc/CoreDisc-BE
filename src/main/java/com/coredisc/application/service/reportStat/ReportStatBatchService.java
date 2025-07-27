package com.coredisc.application.service.reportStat;

import java.time.LocalDate;

public interface ReportStatBatchService {
    void generateDailyStatistics(LocalDate targetDate);
    void generateRandomQuestionsStats(LocalDate targetDate);
    void generateMonthlyFixedQuestionStats(LocalDate targetDate);

    void generateMonthlySelectionDiaryStats(LocalDate targetDate);
}
