package com.coredisc.common.converter;

import com.coredisc.application.service.reportStat.ReportRawData;
import com.coredisc.common.util.DailyEnumMappingHelper;
import com.coredisc.domain.common.enums.TimeZoneType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.reportStats.DailyAnswerHourStat;
import com.coredisc.domain.reportStats.DailyRandomQuestionStat;
import com.coredisc.domain.reportStats.MonthlyFixedQuestionStat;
import com.coredisc.domain.reportStats.MonthlySelectionDiaryStat;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.presentation.dto.reportStat.ReportStatResponseDTO;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReportStatConverter {

    private ReportStatConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static ReportStatResponseDTO.PeakHourDTO toPeakHourDTO(ReportRawData.HourlyAnswerRawData rawData) {
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
        List<ReportStatResponseDTO.SelectedQuestionDTO> questions = rawData.getQuestions().stream()
                .map(data -> ReportStatResponseDTO.SelectedQuestionDTO.builder()
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

                    String dailyTypeContent = DailyEnumMappingHelper.toDailyTypeName(dailyType);
                    String optionContent = DailyEnumMappingHelper.toLabelFromSelectedOption(dailyType, selectedOption);

                    return ReportStatResponseDTO.DailyOptionDTO.builder()
                            .dailyType(dailyTypeContent)
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

    public static List<DailyAnswerHourStat> toDailyAnswerHourStats(List<Post> posts, LocalDate targetDate) {
        return posts.stream()
                .map(post -> DailyAnswerHourStat.builder()
                        .memberId(post.getMember().getId())
                        .answerDate(targetDate)
                        .hourOfDay(post.getCreatedAt().getHour())
                        .answerCount(1)
                        .build())
                .toList();
    }

    public static DailyRandomQuestionStat toDailyRandomQuestionStats(Member member, String questionContent, LocalDate targetDate) {
        return DailyRandomQuestionStat.builder()
                .memberId(member.getId())
                .questionContent(questionContent)
                .selectedDate(targetDate)
                .build();
    }

    public static List<MonthlyFixedQuestionStat> toMonthlyFixedQuestionStats(List<TodayQuestion> questions, Set<Long> memberIds, int year, int month) {
        return questions.stream()
                .filter(q -> memberIds.contains(q.getMember().getId()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                q -> q.getMember().getId() + "-" + q.getQuestionOrder(),
                                q -> MonthlyFixedQuestionStat.builder()
                                        .memberId(q.getMember().getId())
                                        .year(year)
                                        .month(month)
                                        .questionOrder(q.getQuestionOrder())
                                        .questionContent(q.getQuestionContent())
                                        .build(),
                                (existing, replacement) -> existing
                        ),
                        map -> new ArrayList<>(map.values())
                ));
    }

    public static MonthlySelectionDiaryStat toOrUpdateMonthlySelectionDiaryStat(
            Map.Entry<ReportRawData.SelectionStatKey, Integer> entry,
            Map<ReportRawData.SelectionStatKey, MonthlySelectionDiaryStat> existingStatMap) {

        ReportRawData.SelectionStatKey key = entry.getKey();
        int countToAdd = entry.getValue();

        MonthlySelectionDiaryStat existingStat = existingStatMap.get(key);

        if (existingStat != null) {
            existingStat.setSelectionCount(existingStat.getSelectionCount() + countToAdd);
            return existingStat;
        } else {
            // 없으면 새로 생성
            return MonthlySelectionDiaryStat.builder()
                    .memberId(key.getMemberId())
                    .year(key.getYear())
                    .month(key.getMonth())
                    .dailyType(key.getDailyType())
                    .selectedOption(key.getSelectedOption())
                    .selectionCount(countToAdd)
                    .build();
        }
    }

    public static ReportStatResponseDTO.DailyDetailListDTO toDailyDetailListDTO(List<Post> posts) {
        Map<LocalDate, String> detailMap = posts.stream()
                .collect(Collectors.toMap(
                        post -> post.getCreatedAt().toLocalDate(),
                        Post::getDailyDetail,
                        (oldValue, newValue) -> newValue,
                        LinkedHashMap::new
                ));

        return ReportStatResponseDTO.DailyDetailListDTO.builder()
                .dailyDetails(detailMap)
                .build();
    }
}