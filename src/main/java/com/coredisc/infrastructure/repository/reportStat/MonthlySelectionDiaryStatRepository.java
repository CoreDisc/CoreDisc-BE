package com.coredisc.infrastructure.repository.reportStat;

import com.coredisc.domain.reportStats.MonthlySelectionDiaryStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlySelectionDiaryStatRepository extends JpaRepository<MonthlySelectionDiaryStat, Long> {

    //선택형 일기에서 질문 타입에 따라 사용자가 한 달동안 가장 많이 선택된 옵션과 횟수 조회
    @Query("SELECT m.selectedOption, m.selectionCount " +
            "FROM MonthlySelectionDiaryStat m " +
            "WHERE m.memberId = :memberId AND m.year = :year AND m.month = :month AND m.dailyType = :dailyType " +
            "AND m.selectionCount = (" +
            "   SELECT MAX(m2.selectionCount) FROM MonthlySelectionDiaryStat m2 " +
            "   WHERE m2.memberId = :memberId AND m2.year = :year AND m2.month = :month AND m2.dailyType = :dailyType" +
            ")")
    List<Object[]> findTopOptionByMemberYearMonthAndDailyType(
            @Param("memberId") Long memberId,
            @Param("year") int year,
            @Param("month") int month,
            @Param("dailyType") int dailyType);
}