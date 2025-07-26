package com.coredisc.application.service.reportStat;

import com.coredisc.common.util.DateUtil;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import com.coredisc.domain.postAnswer.PostAnswerRepository;
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
import java.util.List;
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
    private final PostAnswerRepository postAnswerRepository;

    private static final long RANDOM_QUESTION_ORDER = 4L;

    @Override
    @Transactional
    public void generateDailyStatistics(LocalDate targetDate) {
        // 그날에 답변한 시간 데이터 저장

        List<Post> posts = postRepository.findPostsByCreatedDate(targetDate);

        List<DailyAnswerHourStat> stats = posts.stream()
                .map(post -> DailyAnswerHourStat.builder()
                        .memberId(post.getMember().getId())
                        .answerDate(targetDate)
                        .hourOfDay(post.getCreatedAt().getHour())
                        .answerCount(1)
                        .build())
                .toList();

        dailyAnswerHourStatRepository.saveAll(stats);
    }

    @Override
    @Transactional
    public void generateRandomQuestionsStats(LocalDate targetDate) {
        // 그 날에 선택한 랜덤 질문 데이터 저장
        LocalDateTime startOfDay = DateUtil.getStartOfDay(targetDate);
        LocalDateTime endOfDay = DateUtil.getEndOfDay(targetDate);

        List<PostAnswer> postAnswers = postAnswerRepository.findByCreatedAtBetweenAndTodayQuestionId(startOfDay, endOfDay, RANDOM_QUESTION_ORDER);

        List<DailyRandomQuestionStat> stats = postAnswers.stream()
                .map(pa -> DailyRandomQuestionStat.builder()
                        .memberId(pa.getPost().getMember().getId())
                        .selectedDate(targetDate)
                        .questionContent(pa.getQuestionContent())
                        .build())
                .toList();

        dailyRandomQuestionStatRepository.saveAll(stats);
    }

    @Override
    @Transactional
    public void generateMonthlyFixedQuestionStats(LocalDate targetDate) {
        YearMonth targetMonth = YearMonth.from(targetDate);
        int year = targetMonth.getYear();
        int month = targetMonth.getMonthValue();

        LocalDateTime startOfMonth = DateUtil.getStartDateTime(year, month);
        LocalDateTime endOfMonth = DateUtil.getEndDateTime(year, month);

        // 이미 저장된 통계 데이터가 있는 멤버 ID 집합 조회
        Set<Long> existingMemberIds = monthlyFixedQuestionStatRepository.findMemberIdsByYearAndMonth(year, month);

        // 해당 월에 처음 포스트를 작성한 사람들의 포스트만 추출
        List<Post> firstPosts = postRepository.findFirstPostPerMemberInMonth(startOfMonth, endOfMonth);

        Set<Long> memberIds = firstPosts.stream()
                .map(post -> post.getMember().getId())
                .filter(memberId -> !existingMemberIds.contains(memberId))
                .collect(Collectors.toSet());

        if (memberIds.isEmpty()) {
            return; // 저장할 신규 멤버가 없으면 바로 종료
        }

        // 해당 월에 작성된 1~3번 고정 질문 가져오기
        List<TodayQuestion> questions = todayQuestionRepository
                .findByQuestionOrderInAndSelectedDateBetween(List.of(1, 2, 3), startOfMonth.toLocalDate(), endOfMonth.toLocalDate());

        // 멤버 별 고정 질문 내용 저장
        List<MonthlyFixedQuestionStat> stats = questions.stream()
                .filter(q -> memberIds.contains(q.getMember().getId()))
                .map(q -> MonthlyFixedQuestionStat.builder()
                        .memberId(q.getMember().getId())
                        .year(year)
                        .month(month)
                        .questionOrder(q.getQuestionOrder())
                        .questionContent(q.getQuestionContent())
                        .build())
                .toList();

        monthlyFixedQuestionStatRepository.saveAll(stats);
    }

    // TODO: 구현 중...
    @Override
    @Transactional
    public void generateMonthlySelectionDiaryStats(LocalDate targetDate) {
        // 그 날에 기록한 선택형 일기 데이터 저장

        List<Post> posts = postRepository.findPostsByCreatedDate(targetDate);

        List<MonthlySelectionDiaryStat> stats = posts.stream().map(post -> MonthlySelectionDiaryStat.builder()
                .memberId(post.getMember().getId())
                .year(post.getCreatedAt().getYear())
                .month(post.getCreatedAt().getMonthValue())
                .build()).toList();
        monthlySelectionDiaryStatRepository.saveAll(stats);
    }
}
