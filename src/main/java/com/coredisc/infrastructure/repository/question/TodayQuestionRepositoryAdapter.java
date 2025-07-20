package com.coredisc.infrastructure.repository.question;


import com.coredisc.domain.member.Member;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.todayQuestion.TodayQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodayQuestionRepositoryAdapter  implements TodayQuestionRepository {

    private final JpaTodayQuestionRepository jpaTodayQuestionRepository;

    @Override
    public TodayQuestion save(TodayQuestion todayQuestion) {
        return jpaTodayQuestionRepository.save(todayQuestion);
    }

    @Override
    public Optional<TodayQuestion> findByMemberAndQuestionOrderAndSelectedDateBetween(Member member, Integer questionOrder, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaTodayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(member, questionOrder, startDate, endDate);
    }

}
