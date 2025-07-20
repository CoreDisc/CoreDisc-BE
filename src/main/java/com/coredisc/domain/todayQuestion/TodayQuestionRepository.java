package com.coredisc.domain.todayQuestion;

import com.coredisc.domain.member.Member;

import java.time.LocalDate;
import java.util.Optional;

public interface TodayQuestionRepository {

    TodayQuestion save(TodayQuestion todayQuestion);

    Optional<TodayQuestion> findByMemberAndQuestionOrderAndSelectedDateBetween( Member member, Integer questionOrder, LocalDate startDate, LocalDate endDate);

    Optional<TodayQuestion> findByMemberAndQuestionOrderAndSelectedDate(Member member, Integer questionOrder, LocalDate selectedDate);
}
