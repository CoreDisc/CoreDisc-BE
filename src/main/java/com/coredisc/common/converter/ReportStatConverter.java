package com.coredisc.common.converter;

import com.coredisc.application.service.report.ReportRawData;
import com.coredisc.domain.common.enums.TimeZoneType;
import com.coredisc.presentation.dto.report.ReportResponseDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReportStatConverter {

    private ReportStatConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static ReportResponseDTO.PeakHourDTO toPeakHourDTO(ReportRawData.HourlyAnswerRawData rawData) {
        // 최다 응답 시간대 찾기 -> 이 부분 고민 중...
        Map.Entry<Integer, Integer> maxEntry = rawData.getHourCountMap().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        int topHour = (maxEntry != null) ? maxEntry.getKey() : -1;
        int maxCount = (maxEntry != null) ? maxEntry.getValue() : -1;

        ReportResponseDTO.HourlyAnswerCountDTO topHours = ReportResponseDTO.HourlyAnswerCountDTO.builder()
                .hour(topHour)
                .answerCount(maxCount)
                .build();

        List<ReportResponseDTO.TimeZoneCountDTO> timeZoneStats = Arrays.stream(TimeZoneType.values())
                .map(type -> new ReportResponseDTO.TimeZoneCountDTO(
                        type,
                        getTimeZoneCount(type, rawData.getHourCountMap())
                )).toList();

        return ReportResponseDTO.PeakHourDTO.builder()
                .year(rawData.getYear())
                .month(rawData.getMonth())
                .topHours(topHours)
                .timeZoneStats(timeZoneStats)
                .build();
    }

    public static ReportResponseDTO.MostSelectedQuestionDTO toMostSelectedQuestionDTO(ReportRawData.MostSelectedQuestionRawData rawData) {
        List<ReportResponseDTO.SeletedQuestionDTO> questions = rawData.getQuestions().stream()
                .map(data -> ReportResponseDTO.SeletedQuestionDTO.builder()
                        .questionContent(data.getQuestionContent())
                        .selectedCount(data.getSelectionCount())
                        .build()
                ).toList();

        return ReportResponseDTO.MostSelectedQuestionDTO.builder()
                .year(rawData.getYear())
                .month(rawData.getMonth())
                .questions(questions)
                .build();
    }

    public static ReportResponseDTO.QuestionListDTO toQuestionListDTO(ReportRawData.QuestionListRawData rawData) {
        List<ReportResponseDTO.QuestionDTO> fixed = rawData.getFixedQuestions().stream()
                .map(stat -> ReportResponseDTO.QuestionDTO.builder()
                        .questionContent(stat.getQuestionContent())
                        .build()
                ).toList();

        List<ReportResponseDTO.QuestionDTO> random = rawData.getRandomQuestions().stream()
                .map(stat -> ReportResponseDTO.QuestionDTO.builder()
                        .questionContent(stat.getQuestionContent())
                        .build()
                ).toList();

        return ReportResponseDTO.QuestionListDTO.builder()
                .year(rawData.getYear())
                .month(rawData.getMonth())
                .fixedQuestions(fixed)
                .randomQuestions(random)
                .build();
    }

    public static ReportResponseDTO.TopDailySelectionDTO toTopDailySelectionDTO(ReportRawData.DailyOptionRawData rawData) {
        List<ReportResponseDTO.DailyOptionDTO> optionDTOList = rawData.getTopSelectedOption().entrySet().stream()
                .map(entry -> {
                    int dailyType = entry.getKey();
                    int selectedOption = entry.getValue().getSelectedOption();
                    int selectionCount = entry.getValue().getSelectionCount();

                    String optionContent = String.valueOf(selectedOption);

                    return ReportResponseDTO.DailyOptionDTO.builder()
                            .dailyType(dailyType)
                            .optionContent(optionContent)
                            .selectionCount(selectionCount)
                            .build();
                })
                .toList();

        return ReportResponseDTO.TopDailySelectionDTO.builder()
                .year(rawData.getYear())
                .month(rawData.getMonth())
                .dailyList(optionDTOList)
                .build();
    }

    private static int getTimeZoneCount(TimeZoneType type, Map<Integer, Integer> hourCountMap) {
        return hourCountMap.entrySet().stream()
                .filter(entry -> type.containsHour(entry.getKey()))
                .mapToInt(Map.Entry::getValue)
                .sum();
    }
}