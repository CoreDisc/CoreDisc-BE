package com.coredisc.domain.monthlyReport;

import com.coredisc.domain.member.Member;

public interface MonthlyReportRepository {

    Long countByMember(Member member);
}
