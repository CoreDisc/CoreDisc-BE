package com.coredisc.application.service.reportStat;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.ReportStatHandler;
import com.coredisc.common.util.DateUtil;
import com.coredisc.domain.reportStats.DailyRandomQuestionStat;
import com.coredisc.domain.reportStats.MonthlyFixedQuestionStat;
import com.coredisc.infrastructure.repository.reportStat.DailyAnswerHourStatRepository;
import com.coredisc.infrastructure.repository.reportStat.DailyRandomQuestionStatRepository;
import com.coredisc.infrastructure.repository.reportStat.MonthlyFixedQuestionStatRepository;
import com.coredisc.infrastructure.repository.reportStat.MonthlySelectionDiaryStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ReportStatQueryServiceImpl implements ReportStatQueryService{

    private final DailyAnswerHourStatRepository answerHourStatRepository;
    private final DailyRandomQuestionStatRepository randomQuestionRepository;
    private final MonthlyFixedQuestionStatRepository fixedQuestionRepository;
    private final MonthlySelectionDiaryStatRepository monthlySelectionDiaryStatRepository;

    @Override
    public ReportRawData.QuestionListRawData getQuestionList(int year, int month, Long memberId) {
        List<MonthlyFixedQuestionStat> fixedQuestions = fixedQuestionRepository.findByMemberIdAndYearAndMonthOrderByQuestionOrder(
                memberId, year, month);
        if(fixedQuestions.isEmpty()) {
            throw new ReportStatHandler(ErrorStatus.STATS_NOT_FOUND);
        }

        LocalDate start = DateUtil.getStartDate(year, month);
        LocalDate end = DateUtil.getEndDate(year, month);

        List<DailyRandomQuestionStat> randomQuestions = randomQuestionRepository.findByMemberIdAndSelectedDateRange(
                memberId, start, end);

        return new ReportRawData.QuestionListRawData(year, month, fixedQuestions, randomQuestions);
    }

    @Override
    public ReportRawData.MostSelectedQuestionRawData getMostSelectedQuestions(LocalDate startDate, LocalDate endDate, Long memberId) {
        Pageable top3 = PageRequest.of(0, 3);
        List<Object[]> results = randomQuestionRepository.findTop3QuestionsByMemberAndDateRange(memberId, startDate, endDate, top3);
        if(results.isEmpty()) {
            throw new ReportStatHandler(ErrorStatus.STATS_NOT_FOUND);
        }

        List<ReportRawData.MostSelectedQuestionItem> topQuestions = new ArrayList<>();
        for (Object[] row : results) {
            String questionContent = (String) row[0];
            int selectionCount = ((Number) row[1]).intValue();

            topQuestions.add(new ReportRawData.MostSelectedQuestionItem(questionContent, selectionCount));
        }
        return new ReportRawData.MostSelectedQuestionRawData(startDate.getYear(), startDate.getMonthValue(), topQuestions);
    }

    @Override
    public ReportRawData.HourlyAnswerRawData getHourlyAnswerCountMap(LocalDate startDate, LocalDate endDate, Long memberId) {
        List<Object[]> results = answerHourStatRepository.findHourlyAnswerCountsByMemberIdAndDateRange(memberId, startDate, endDate);
        if(results.isEmpty()) {
            throw new ReportStatHandler(ErrorStatus.STATS_NOT_FOUND);
        }

        Map<Integer, Integer> hourCountMap = new HashMap<>();
        for (Object[] row : results) {
            int hour = ((Number) row[0]).intValue();
            int count = ((Number) row[1]).intValue();
            hourCountMap.put(hour, count);
        }

        for (int i = 0; i < 24; i++) {
            hourCountMap.putIfAbsent(i, 0);
        }

        return new ReportRawData.HourlyAnswerRawData(startDate.getYear(), startDate.getMonthValue(), hourCountMap);
    }


    @Override
    public ReportRawData.DailyOptionRawData getMostSelectedDaily(int year, int month, Long memberId) {
        Map<Integer, ReportRawData.SelectedOptionWithCount> topOptionMap = new HashMap<>();

        for (int dailyType = 1; dailyType <= 3; dailyType++) {
            List<Object[]> result = monthlySelectionDiaryStatRepository
                    .findTopOptionByMemberYearMonthAndDailyType(memberId, year, month, dailyType);

            if (!result.isEmpty()) {
                Object[] row = result.get(0);
                int selectedOption = ((Number) row[0]).intValue();
                int selectedCount = ((Number) row[1]).intValue();

                topOptionMap.put(dailyType, new ReportRawData.SelectedOptionWithCount(selectedOption, selectedCount));
            }
        }

        return new ReportRawData.DailyOptionRawData(year, month, topOptionMap);
    }
}