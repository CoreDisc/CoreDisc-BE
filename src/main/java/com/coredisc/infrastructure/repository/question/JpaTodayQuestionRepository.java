package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTodayQuestionRepository extends JpaRepository<TodayQuestion,Long> {

    List<TodayQuestion> findByMember(Member member);

}
