package com.coredisc.infrastructure.repository.stats;

import com.coredisc.domain.stats.MonthlyFixedQuestionStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyFixedQuestionStatRepository extends JpaRepository<MonthlyFixedQuestionStat, Long> {

    // 특정 월의 고정 질문 3개를 순서대로 조회
    @Query("SELECT m FROM MonthlyFixedQuestionStat m " +
            "WHERE m.memberId = :memberId " +
            "AND m.year = :year " +
            "AND m.month = :month " +
            "ORDER BY m.questionOrder ASC")
    List<MonthlyFixedQuestionStat> findByMemberIdAndYearAndMonthOrderByQuestionOrder(
            @Param("memberId") Long memberId,
            @Param("year") int year,
            @Param("month") int month);
}
