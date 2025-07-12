package com.coredisc.application.service.report;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.ReportStatsHandler;
import com.coredisc.common.util.DateUtil;
import com.coredisc.domain.stats.DailyRandomQuestionStat;
import com.coredisc.domain.stats.MonthlyFixedQuestionStat;
import com.coredisc.infrastructure.repository.stats.DailyAnswerHourStatRepository;
import com.coredisc.infrastructure.repository.stats.DailyRandomQuestionStatRepository;
import com.coredisc.infrastructure.repository.stats.MonthlyFixedQuestionStatRepository;
import com.coredisc.infrastructure.repository.stats.MonthlySelectionDiaryStatRepository;
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
//    private final PostRepository postRepository;

    @Override
    public ReportRawData.QuestionListRawData getQuestionList(int year, int month, Long memberId) {
        List<MonthlyFixedQuestionStat> fixedQuestions = fixedQuestionRepository.findByMemberIdAndYearAndMonthOrderByQuestionOrder(
                memberId, year, month);
        if(fixedQuestions.isEmpty()) {
            throw new ReportStatsHandler(ErrorStatus.STATS_NOT_FOUND);
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
            throw new ReportStatsHandler(ErrorStatus.STATS_NOT_FOUND);
        }

        List<ReportRawData.MostSelectedQuestionItem> topQuestions = new ArrayList<>();
        for (Object[] row : results) {
            Long questionId = (Long) row[0];
            String questionContent = (String) row[1];
            int selectionCount = ((Number) row[2]).intValue();

            topQuestions.add(new ReportRawData.MostSelectedQuestionItem(questionId, questionContent, selectionCount));
        }
        return new ReportRawData.MostSelectedQuestionRawData(startDate.getYear(), startDate.getMonthValue(), topQuestions);
    }

    @Override
    public ReportRawData.HourlyAnswerRawData getHourlyAnswerCountMap(LocalDate startDate, LocalDate endDate, Long memberId) {
        List<Object[]> results = answerHourStatRepository.findHourlyAnswerCountsByMemberIdAndDateRange(memberId, startDate, endDate);
        if(results.isEmpty()) {
            throw new ReportStatsHandler(ErrorStatus.STATS_NOT_FOUND);
        }

        Map<Integer, Integer> hourCountMap = new HashMap<>();
        for (Object[] row : results) {
            Integer hour = (Integer) row[0];
            Long count = (Long) row[1];
            hourCountMap.put(hour, count.intValue());
        }

        for (int i = 0; i < 24; i++) {
            hourCountMap.putIfAbsent(i, 0);
        }

        return new ReportRawData.HourlyAnswerRawData(startDate.getYear(), startDate.getMonthValue(), hourCountMap);
    }

    //여기부터는 아직 미정, 추후 수정 예정
    @Override
    public List<ReportRawData.DailyOptionRawData> getTopDailyOptions(int year, int month, Long memberId) {
        List<ReportRawData.DailyOptionRawData> resultList = new ArrayList<>();

        for (int dailyType = 1; dailyType <= 3; dailyType++) {
            List<Object[]> rawList = monthlySelectionDiaryStatRepository.findTopOptionByMemberYearMonthAndDailyType(memberId, year, month, dailyType);
            for (Object[] row : rawList) {
                String optionContent = (String) row[0];
                Integer selectionCount = ((Number) row[1]).intValue();

                resultList.add(new ReportRawData.DailyOptionRawData(year, month, dailyType, optionContent, selectionCount));
            }
        }
        return resultList;
    }

    @Override
    public ReportRawData.DailyDetailRawData getDailyDetails(int year, int month, Long memberId) {
        return null;
    }
}