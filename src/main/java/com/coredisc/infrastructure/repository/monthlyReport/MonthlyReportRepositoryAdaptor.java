package com.coredisc.infrastructure.repository.monthlyReport;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.monthlyReport.MonthlyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MonthlyReportRepositoryAdaptor implements MonthlyReportRepository {

    private final JpaMonthlyReportRepository jpaMonthlyReportRepository;

    @Override
    public Long countByMember(Member member) {

        return jpaMonthlyReportRepository.countByMember(member);
    }
}
