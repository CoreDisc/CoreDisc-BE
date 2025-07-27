package com.coredisc.application.service.disc;

import java.time.LocalDate;

public interface DiscBatchService {
    void generateDiscsForMonth(LocalDate targetMonth);
}
