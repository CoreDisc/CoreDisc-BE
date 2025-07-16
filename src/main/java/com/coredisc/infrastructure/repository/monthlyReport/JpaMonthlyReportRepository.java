package com.coredisc.infrastructure.repository.monthlyReport;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.monthlyReport.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {

    Long countByMember(Member member);
}
