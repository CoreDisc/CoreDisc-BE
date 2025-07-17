package com.coredisc.common.converter;

import com.coredisc.application.service.reportStat.ReportRawData;
import com.coredisc.domain.common.enums.TimeZoneType;
import com.coredisc.presentation.dto.reportStat.ReportStatResponseDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReportStatConverter {

    private ReportStatConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static ReportStatResponseDTO.PeakHourDTO toPeakHourDTO(ReportRawData.HourlyAnswerRawData rawData) {
        // 최다 응답 시간대 찾기 -> 이 부분 고민 중...
        Map.Entry<Integer, Integer> maxEntry = rawData.getHourCountMap().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        int topHour = (maxEntry != null) ? maxEntry.getKey() : -1;
        int maxCount = (maxEntry != null) ? maxEntry.getValue() : -1;

        ReportStatResponseDTO.HourlyAnswerCountDTO topHours = ReportStatResponseDTO.HourlyAnswerCountDTO.builder()
                .hour(topHour)
                .answerCount(maxCount)
                .build();

        List<ReportStatResponseDTO.TimeZoneCountDTO> timeZoneStats = Arrays.stream(TimeZoneType.values())
                .map(type -> new ReportStatResponseDTO.TimeZoneCountDTO(
                        type,
                        getTimeZoneCount(type, rawData.getHourCountMap())
                )).toList();

        return ReportStatResponseDTO.PeakHourDTO.builder()
                .year(rawData.getYear())
                .month(rawData.getMonth())
                .topHours(topHours)
                .timeZoneStats(timeZoneStats)
                .build();
    }

    public static ReportStatResponseDTO.MostSelectedQuestionDTO toMostSelectedQuestionDTO(ReportRawData.MostSelectedQuestionRawData rawData) {
        List<ReportStatResponseDTO.SeletedQuestionDTO> questions = rawData.getQuestions().stream()
                .map(data -> ReportStatResponseDTO.SeletedQuestionDTO.builder()
                        .questionContent(data.getQuestionContent())
                        .selectedCount(data.getSelectionCount())
                        .build()
                ).toList();

        return ReportStatResponseDTO.MostSelectedQuestionDTO.builder()
                .year(rawData.getYear())
                .month(rawData.getMonth())
                .questions(questions)
                .build();
    }

    public static ReportStatResponseDTO.QuestionListDTO toQuestionListDTO(ReportRawData.QuestionListRawData rawData) {
        List<ReportStatResponseDTO.QuestionDTO> fixed = rawData.getFixedQuestions().stream()
                .map(stat -> ReportStatResponseDTO.QuestionDTO.builder()
                        .questionContent(stat.getQuestionContent())
                        .build()
                ).toList();

        List<ReportStatResponseDTO.QuestionDTO> random = rawData.getRandomQuestions().stream()
                .map(stat -> ReportStatResponseDTO.QuestionDTO.builder()
                        .questionContent(stat.getQuestionContent())
                        .build()
                ).toList();

        return ReportStatResponseDTO.QuestionListDTO.builder()
                .year(rawData.getYear())
                .month(rawData.getMonth())
                .fixedQuestions(fixed)
                .randomQuestions(random)
                .build();
    }

    public static ReportStatResponseDTO.TopDailySelectionDTO toTopDailySelectionDTO(ReportRawData.DailyOptionRawData rawData) {
        List<ReportStatResponseDTO.DailyOptionDTO> optionDTOList = rawData.getTopSelectedOption().entrySet().stream()
                .map(entry -> {
                    int dailyType = entry.getKey();
                    int selectedOption = entry.getValue().getSelectedOption();
                    int selectionCount = entry.getValue().getSelectionCount();

                    String optionContent = String.valueOf(selectedOption);

                    return ReportStatResponseDTO.DailyOptionDTO.builder()
                            .dailyType(dailyType)
                            .optionContent(optionContent)
                            .selectionCount(selectionCount)
                            .build();
                })
                .toList();

        return ReportStatResponseDTO.TopDailySelectionDTO.builder()
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