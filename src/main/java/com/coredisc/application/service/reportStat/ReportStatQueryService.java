package com.coredisc.application.service.reportStat;

import java.time.LocalDate;

public interface ReportStatQueryService {

    // 기간별 전체 질문 목록 조회 (고정 질문 + 랜덤 질문)
    ReportRawData.QuestionListRawData getQuestionList(int year, int month, Long memberId);

    // 기간별 최다 선택된 랜덤 질문 조회
    ReportRawData.MostSelectedQuestionRawData getMostSelectedQuestions(int year, int month, Long memberId);

    // 기간별 응답 시간대 횟수 조회
    ReportRawData.HourlyAnswerRawData getHourlyAnswerCountMap(int year, int month, Long memberId);

    // 기간별 게시글의 daily_ 항목 최다 답변 조회
    ReportRawData.DailyOptionRawData getMostSelectedDaily(int year, int month, Long memberId);
}
