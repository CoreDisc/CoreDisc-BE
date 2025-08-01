package com.coredisc.application.service.reportStat;

import com.coredisc.common.converter.ReportStatConverter;
import com.coredisc.common.util.DailyEnumMappingHelper;
import com.coredisc.common.util.DateUtil;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.reportStats.*;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.todayQuestion.TodayQuestionRepository;
import com.coredisc.infrastructure.repository.reportStat.DailyAnswerHourStatRepository;
import com.coredisc.infrastructure.repository.reportStat.DailyRandomQuestionStatRepository;
import com.coredisc.infrastructure.repository.reportStat.MonthlyFixedQuestionStatRepository;
import com.coredisc.infrastructure.repository.reportStat.MonthlySelectionDiaryStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportStatBatchServiceImpl implements ReportStatBatchService {

    private final DailyAnswerHourStatRepository dailyAnswerHourStatRepository;
    private final DailyRandomQuestionStatRepository dailyRandomQuestionStatRepository;
    private final MonthlySelectionDiaryStatRepository monthlySelectionDiaryStatRepository;
    private final MonthlyFixedQuestionStatRepository monthlyFixedQuestionStatRepository;

    private final PostRepository postRepository;
    private final TodayQuestionRepository todayQuestionRepository;

    @Override
    @Transactional
    public void generateDailyStatistics(LocalDate targetDate) {

        List<Post> posts = postRepository.findPostsByCreatedDate(targetDate);
        List<DailyAnswerHourStat> stats = ReportStatConverter.toDailyAnswerHourStats(posts, targetDate);

        dailyAnswerHourStatRepository.saveAll(stats);
    }

    @Override
    @Transactional
    public void generateRandomQuestionsStats(LocalDate targetDate) {
        // 그 날에 선택한 랜덤 질문 데이터 저장
        LocalDateTime startOfDay = DateUtil.getStartOfDay(targetDate);
        LocalDateTime endOfDay = DateUtil.getEndOfDay(targetDate);

        // 포스트 작성한 멤버만 조회해서 랜덤 질문 선택 내역 저장
        List<Member> members = postRepository.findMembersByPostCreatedAtBetween(startOfDay, endOfDay);
        List<TodayQuestion> randomQuestions = todayQuestionRepository.findAllByQuestionOrderAndSelectedDate(4, targetDate);

        Map<Long, TodayQuestion> questionMap = randomQuestions.stream()
                .collect(Collectors.toMap(
                        tq -> tq.getMember().getId(),
                        tq -> tq
                ));

        List<DailyRandomQuestionStat> stats = members.stream()
                .map(member -> {
                    TodayQuestion tq = questionMap.get(member.getId());
                    if (tq == null) return null;  // null이면 건너뛰기 (실제로는 발생 가능성 X, 개발 중 에러 발생 가능하므로)
                    String question = tq.getQuestionContent();
                    return ReportStatConverter.toDailyRandomQuestionStats(member, question, targetDate);
                })
                .collect(Collectors.toList());

        dailyRandomQuestionStatRepository.saveAll(stats);
    }

    @Override
    @Transactional
    public void generateMonthlyFixedQuestionStats(LocalDate targetDate) {
        YearMonth targetMonth = YearMonth.from(targetDate);
        int year = targetMonth.getYear();
        int month = targetMonth.getMonthValue();

        LocalDateTime startOfMonth = DateUtil.getStartDateTime(year, month);
        LocalDateTime startOfDay = DateUtil.getStartOfDay(targetDate);
        LocalDateTime endOfDay = DateUtil.getEndOfDay(targetDate);

        // 오늘 작성한 모든 포스트 멤버 ID
        List<Long> membersWhoPostedToday = postRepository.findDistinctMemberIdsByCreatedAtBetween(startOfDay, endOfDay);

        // 이번 달 1일부터 오늘 전날까지 포스트 작성한 멤버 IDs
        List<Long> membersWithPostsBeforeToday = postRepository.findDistinctMemberIdsByCreatedAtBetween(startOfMonth, startOfDay.minusSeconds(1));

        // 오늘 처음 작성한 멤버만 필터링
        Set<Long> newMemberIds = membersWhoPostedToday.stream()
                .filter(memberId -> !membersWithPostsBeforeToday.contains(memberId))
                .collect(Collectors.toSet());

        // 고정 질문 조회

        List<TodayQuestion> questions = todayQuestionRepository
                .findByMemberIdInAndQuestionOrderInAndSelectedDateBetween(
                        new ArrayList<>(newMemberIds),
                        List.of(1, 2, 3),
                        targetMonth.atDay(1),
                        targetMonth.atEndOfMonth());

        List<MonthlyFixedQuestionStat> stats = ReportStatConverter.toMonthlyFixedQuestionStats(questions, newMemberIds, year, month);

        monthlyFixedQuestionStatRepository.saveAll(stats);
    }

    @Override
    @Transactional
    public void generateMonthlySelectionDiaryStats(LocalDate targetDate) {
        List<Post> posts = postRepository.findPostsByCreatedDate(targetDate);
        Map<ReportRawData.SelectionStatKey, Integer> aggregationMap = getAggregationMap(posts);

        List<MonthlySelectionDiaryStat> existingStats = monthlySelectionDiaryStatRepository
                .findAllByYearAndMonth(targetDate.getYear(), targetDate.getMonthValue());

        Map<ReportRawData.SelectionStatKey, MonthlySelectionDiaryStat> existingStatMap = existingStats.stream()
                .collect(Collectors.toMap(
                        stat -> new ReportRawData.SelectionStatKey(
                                stat.getMemberId(),
                                stat.getYear(),
                                stat.getMonth(),
                                stat.getDailyType(),
                                stat.getSelectedOption()
                        ),
                        stat -> stat
                ));

        List<MonthlySelectionDiaryStat> statsToSave = aggregationMap.entrySet().stream()
                .map(entry -> ReportStatConverter.toOrUpdateMonthlySelectionDiaryStat(entry, existingStatMap))
                .toList();

        monthlySelectionDiaryStatRepository.saveAll(statsToSave);
    }


    private static Map<ReportRawData.SelectionStatKey, Integer> getAggregationMap(List<Post> posts) {
        Map<ReportRawData.SelectionStatKey, Integer> aggregationMap = new HashMap<>();

        for (Post post : posts) {
            Long memberId = post.getMember().getId();
            int year = post.getCreatedAt().getYear();
            int month = post.getCreatedAt().getMonthValue();

            int whoOption = DailyEnumMappingHelper.toSelectedOption(post.getDailyWho());
            ReportRawData.SelectionStatKey keyWho = new ReportRawData.SelectionStatKey(memberId, year, month, 1, whoOption);
            aggregationMap.put(keyWho, aggregationMap.getOrDefault(keyWho, 0) + 1);

            int whereOption = DailyEnumMappingHelper.toSelectedOption(post.getDailyWhere());
            ReportRawData.SelectionStatKey keyWhere = new ReportRawData.SelectionStatKey(memberId, year, month, 2, whereOption);
            aggregationMap.put(keyWhere, aggregationMap.getOrDefault(keyWhere, 0) + 1);

            int whatOption = DailyEnumMappingHelper.toSelectedOption(post.getDailyWhat());
            ReportRawData.SelectionStatKey keyWhat = new ReportRawData.SelectionStatKey(memberId, year, month, 3, whatOption);
            aggregationMap.put(keyWhat, aggregationMap.getOrDefault(keyWhat, 0) + 1);
        }

        return aggregationMap;
    }
}
