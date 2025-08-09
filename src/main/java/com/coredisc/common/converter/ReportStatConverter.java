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
        if (questions == null || questions.isEmpty()) {
            return new ArrayList<>();
        }
        
        Map<String, MonthlyFixedQuestionStat> statMap = new HashMap<>();
        
        for (TodayQuestion question : questions) {
            if (question.getMember() == null || !memberIds.contains(question.getMember().getId())) {
                continue;
            }
            
            String key = question.getMember().getId() + "-" + question.getQuestionOrder();
            
            MonthlyFixedQuestionStat stat = MonthlyFixedQuestionStat.builder()
                .memberId(question.getMember().getId())
                .year(year)
                .month(month)
                .questionOrder(question.getQuestionOrder())
                .questionContent(question.getQuestionContent())
                .build();
                
            statMap.put(key, stat);
        }
        
        return new ArrayList<>(statMap.values());
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
                .filter(post -> post.getDailyDetail() != null && !post.getDailyDetail().isBlank())  // dailyDetail이 비어있지 않은 경우만
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

    public static ReportStatResponseDTO.MonthlyReportDTO toMonthlyReport(ReportRawData.MonthlyReportRawData rawData) {

        int year = rawData.getYear();
        int month = rawData.getMonth();

        ReportRawData.QuestionListRawData questionListRaw = rawData.getQuestionListRaw();
        ReportRawData.MostSelectedQuestionRawData mostSelectedRaw = rawData.getMostSelectedRaw();
        ReportRawData.HourlyAnswerRawData peakHourRaw = rawData.getPeakHourRaw();

        List<ReportStatResponseDTO.QuestionDTO> fixed = questionListRaw.getFixedQuestions().stream()
                .map(stat -> ReportStatResponseDTO.QuestionDTO.builder()
                        .questionContent(stat.getQuestionContent())
                        .build()
                ).toList();

        List<ReportStatResponseDTO.QuestionDTO> random = questionListRaw.getRandomQuestions().stream()
                .map(DailyRandomQuestionStat::getQuestionContent)
                .distinct()
                .map(content -> ReportStatResponseDTO.QuestionDTO.builder()
                        .questionContent(content)
                        .build())
                .toList();

        boolean isAllOneCount = mostSelectedRaw.isAllOneCount();
        List<ReportStatResponseDTO.SelectedQuestionDTO> mostSelected = Collections.emptyList();

        if(!isAllOneCount){
            mostSelected = mostSelectedRaw.getQuestions().stream()
                    .map(data -> ReportStatResponseDTO.SelectedQuestionDTO.builder()
                            .questionContent(data.getQuestionContent())
                            .selectedCount(data.getSelectionCount())
                            .build()
                    ).toList();
        }

        TimeZoneType peakTime = peakHourRaw.getHourCountMap().entrySet().stream()
                .collect(Collectors.groupingBy(
                        e -> TimeZoneType.fromHour(e.getKey()),
                        () -> new EnumMap<>(TimeZoneType.class),
                        Collectors.summingInt(Map.Entry::getValue)
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return ReportStatResponseDTO.MonthlyReportDTO.builder()
                .year(year)
                .month(month)
                .fixedQuestions(fixed)
                .randomQuestions(random)
                .isAllOneCount(isAllOneCount)
                .mostSelectedQuestions(mostSelected)
                .peakTimeZone(peakTime)
                .build();
    }
}