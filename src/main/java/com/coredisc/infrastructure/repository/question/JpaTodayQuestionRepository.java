package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaTodayQuestionRepository extends JpaRepository<TodayQuestion,Long> {

    List<TodayQuestion> findByMember(Member member);

    Optional<TodayQuestion> findByMemberAndQuestionOrderAndSelectedDateBetween(Member member, Integer questionOrder, LocalDate startDate, LocalDate endDate);

    Optional<TodayQuestion> findByMemberAndQuestionOrderAndSelectedDate(Member member, Integer questionOrder, LocalDate selectedDate);
}
