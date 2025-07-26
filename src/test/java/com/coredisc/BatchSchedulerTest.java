package com.coredisc;

import com.coredisc.infrastructure.schedule.BatchScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class BatchSchedulerTest {

    private final BatchScheduler batchScheduler;

    @Autowired
    public BatchSchedulerTest(BatchScheduler batchScheduler) {
        this.batchScheduler = batchScheduler;
    }

    @Test
    public void testRunBatchForMonth() {
        LocalDate targetMonth = LocalDate.of(2025, 7, 1);
        batchScheduler.runBatchForMonth(targetMonth);
    }

    @Test
    public void testrunBatchForDay(){
        LocalDate targetDate = LocalDate.of(2025, 7, 26);
        batchScheduler.runBatchForDay(targetDate);
    }
}