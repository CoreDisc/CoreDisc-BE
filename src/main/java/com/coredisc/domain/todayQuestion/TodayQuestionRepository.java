package com.coredisc.domain.todayQuestion;

import com.coredisc.domain.member.Member;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodayQuestionRepository {

    TodayQuestion save(TodayQuestion todayQuestion);

    Optional<TodayQuestion> findByMemberAndQuestionOrderAndSelectedDateBetween( Member member, Integer questionOrder, LocalDateTime startDate, LocalDateTime endDate);
}
