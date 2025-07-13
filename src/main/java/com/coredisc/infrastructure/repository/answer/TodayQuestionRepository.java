package com.coredisc.infrastructure.repository.answer;

import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodayQuestionRepository extends JpaRepository<TodayQuestion,Long> {

    List<TodayQuestion> findByMember(Member member);

}
