package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaMemberOfficialQuestionRepository extends JpaRepository<MemberOfficialQuestion, Long> {

    Optional<MemberOfficialQuestion> findByMemberAndId(Member member, Long id);
}
