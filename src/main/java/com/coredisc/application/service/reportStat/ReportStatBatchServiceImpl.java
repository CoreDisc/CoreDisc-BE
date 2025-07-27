package com.coredisc.application.service.reportStat;

import com.coredisc.common.converter.ReportStatConverter;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        // 그날에 답변한 시간 데이터 저장

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

        // 컨버터로 변환
        List<MonthlyFixedQuestionStat> stats = ReportStatConverter.toMonthlyFixedQuestionStats(questions, newMemberIds, year, month);

        // 저장
        monthlyFixedQuestionStatRepository.saveAll(stats);
    }

    // TODO: 구현 중...
    @Override
    @Transactional
    public void generateMonthlySelectionDiaryStats(LocalDate targetDate) {
        // 그 날에 기록한 선택형 일기 데이터 저장

        List<Post> posts = postRepository.findPostsByCreatedDate(targetDate);

        // TODO: 컨버터로 분리하기
        List<MonthlySelectionDiaryStat> stats = posts.stream().map(post -> MonthlySelectionDiaryStat.builder()
                .memberId(post.getMember().getId())
                .year(post.getCreatedAt().getYear())
                .month(post.getCreatedAt().getMonthValue())
                .build()).toList();
        monthlySelectionDiaryStatRepository.saveAll(stats);
    }
}
