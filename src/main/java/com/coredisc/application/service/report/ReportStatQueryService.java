package com.coredisc.application.service.report;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportStatQueryService {

    // 기간별 전체 질문 목록 조회 (고정 질문 + 랜덤 질문)
    ReportRawData.QuestionListRawData getQuestionList(int year, int month, Long memberId);

    // 기간별 최다 선택된 랜덤 질문 조회
    ReportRawData.MostSelectedQuestionRawData getMostSelectedQuestions(LocalDate startDate, LocalDate endDate, Long memberId);

    // 기간별 응답 시간대 횟수 조회
    ReportRawData.HourlyAnswerRawData getHourlyAnswerCountMap(LocalDate startDate, LocalDate endDate, Long memberId);

    // 기간별 게시글의 daily_ 항목 최다 답변 조회
    List<ReportRawData.DailyOptionRawData> getTopDailyOptions(int year, int month, Long memberId);

    // 사용자가 특정 달에 작성한 일기 내용 전체 출력
    ReportRawData.DailyDetailRawData getDailyDetails(int year, int month, Long memberId);

}
