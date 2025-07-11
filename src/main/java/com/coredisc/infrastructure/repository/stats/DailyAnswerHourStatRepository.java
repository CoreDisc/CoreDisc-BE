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

    //특정 사용자가 한 달동안 특정 시간 사이에 답변한 횟수 조회
    @Query("SELECT SUM(d.answerCount) FROM DailyAnswerHourStat d " +
            "WHERE d.memberId = :memberId " +
            "AND d.answerDate BETWEEN :startDate AND :endDate " +
            "AND d.hourOfDay BETWEEN :startHour AND :endHour")
    Integer findAnswerCountByMemberIdAndDateRangeAndHourRange(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("startHour") int startHour,
            @Param("endHour") int endHour);

    //데이터 중복 체크용
    boolean existsByMemberIdAndAnswerDateAndHourOfDay(Long memberId, LocalDate answerDate, int hourOfDay);
}
