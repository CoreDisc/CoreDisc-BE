package com.coredisc.application.service.reportStat;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;

import java.util.List;

public interface ReportStatQueryService {

    // 웗별 리포트 rawData 조회
    ReportRawData.MonthlyReportRawData getMonthlyReportRawData(int year, int month, Long memberId);

    // 기간별 게시글의 daily_ 항목 최다 답변 조회
    ReportRawData.DailyOptionRawData getMostSelectedDaily(int year, int month, Long memberId);

    // 기간별 daily_detail 항목 답변 목록 조회
    List<Post> getDailyDetails(int year, int month, Member member);
}
