package com.coredisc.infrastructure.repository.stats;

import com.coredisc.domain.common.enums.TimeZoneType;
import com.coredisc.domain.stats.DailyAnswerHourStat;
import com.coredisc.presentation.dto.report.ReportResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyAnswerHourStatRepository extends JpaRepository<DailyAnswerHourStat, Long> {

    // 특정 회원이 특정 월에 가장 많이 답변한 시간대+횟수를 출력
    @Query("SELECT new com.coredisc.presentation.dto.report.ReportResponseDTO.HourlyAnswerCountDTO(" +
            "d.hourOfDay, CAST(SUM(d.answerCount) AS int)) " +
            "FROM DailyAnswerHourStat d " +
            "WHERE d.memberId = :memberId " +
            "AND YEAR(d.answerDate) = :year " +
            "AND MONTH(d.answerDate) = :month " +
            "GROUP BY d.hourOfDay " +
            "ORDER BY SUM(d.answerCount) DESC")
    List<ReportResponseDTO.HourlyAnswerCountDTO> findPeakHoursByMemberIdAndYearMonth(
            @Param("memberId") Long memberId,
            @Param("year") int year,
            @Param("month") int month,
            Pageable pageable);

    // 지정한 시간 범위(3-5시 등) 내 답변수 합계를 timeZoneType 이름과 함께 반환
    @Query("SELECT new com.coredisc.presentation.dto.report.ReportResponseDTO.TimeZoneCountDTO(" +
            ":timeZoneType, CAST(SUM(d.answerCount) AS int)) " +
            "FROM DailyAnswerHourStat d " +
            "WHERE d.memberId = :memberId " +
            "AND YEAR(d.answerDate) = :year " +
            "AND MONTH(d.answerDate) = :month " +
            "AND d.hourOfDay >= :start " +
            "AND d.hourOfDay <= :end")
    List<ReportResponseDTO.TimeZoneCountDTO> findTimeZoneCountByMemberIdAndYearMonthAndHourRange(
            @Param("memberId") Long memberId,
            @Param("year") int year,
            @Param("month") int month,
            @Param("start") int start,
            @Param("end") int end,
            @Param("timeZoneType") TimeZoneType timeZoneType
    );

    //데이터 중복 체크용
    boolean existsByMemberIdAndAnswerDateAndHourOfDay(Long memberId, LocalDate answerDate, int hourOfDay);
}
