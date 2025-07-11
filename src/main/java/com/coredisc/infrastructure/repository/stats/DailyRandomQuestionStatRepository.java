package com.coredisc.infrastructure.repository.stats;

import com.coredisc.domain.stats.DailyRandomQuestionStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyRandomQuestionStatRepository extends JpaRepository<DailyRandomQuestionStat, Long> {

    // 특정 월의 모든 랜덤 질문들을 날짜 순으로 조회
    @Query("SELECT d FROM DailyRandomQuestionStat d " +
            "WHERE d.memberId = :memberId " +
            "AND YEAR(d.selectedDate) = :year " +
            "AND MONTH(d.selectedDate) = :month " +
            "ORDER BY d.selectedDate ASC")
    List<DailyRandomQuestionStat> findByMemberIdAndYearAndMonthOrderBySelectedDate(
            @Param("memberId") Long memberId,
            @Param("year") int year,
            @Param("month") int month);
}
