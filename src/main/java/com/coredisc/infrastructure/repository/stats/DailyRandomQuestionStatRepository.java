package com.coredisc.infrastructure.repository.stats;

import com.coredisc.domain.stats.DailyRandomQuestionStat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyRandomQuestionStatRepository extends JpaRepository<DailyRandomQuestionStat, Long> {

    // 특정 월의 모든 랜덤 질문들을 날짜 순으로 조회
    //TODO : 여기 지금 날짜순으로 정렬만 했는데, 만약 중복 질문은 X라면 수정해야 함
    @Query("SELECT d FROM DailyRandomQuestionStat d " +
            "WHERE d.memberId = :memberId " +
            "AND d.selectedDate BETWEEN :startDate AND :endDate " +
            "ORDER BY d.selectedDate ASC")
    List<DailyRandomQuestionStat> findByMemberIdAndSelectedDateRange(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    //특정 기간 동안 선택횟수가 가장 많은 3개의 랜덤 질문 내용과 선택된 횟수를 조회, 동순위는 가나다순 처리
    @Query("SELECT d.questionContent, COUNT(d) as selectionCount " +
            "FROM DailyRandomQuestionStat d " +
            "WHERE d.memberId = :memberId " +
            "AND d.selectedDate BETWEEN :startDate AND :endDate " +
            "GROUP BY d.questionContent " +
            "ORDER BY selectionCount DESC, d.questionContent ASC")
    List<Object[]> findTop3QuestionsByMemberAndDateRange(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}
