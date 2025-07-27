package com.coredisc.domain.todayQuestion;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.personalQuestion.PersonalQuestion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodayQuestionRepository {

    TodayQuestion save(TodayQuestion todayQuestion);

    Optional<TodayQuestion> findByMemberAndQuestionOrderAndSelectedDateBetween( Member member, Integer questionOrder, LocalDate startDate, LocalDate endDate);

    Optional<TodayQuestion> findByMemberAndQuestionOrderAndSelectedDate(Member member, Integer questionOrder, LocalDate selectedDate);

    boolean existsByPersonalQuestion(PersonalQuestion personalQuestion);

    List<TodayQuestion> findAllByQuestionOrderAndSelectedDate(int questionOrder, LocalDate targetDate);
    List<TodayQuestion> findByMemberIdInAndQuestionOrderInAndSelectedDateBetween(List<Long> memberIds, List<Integer> questionOrders, LocalDate startDate, LocalDate endDate);

}
