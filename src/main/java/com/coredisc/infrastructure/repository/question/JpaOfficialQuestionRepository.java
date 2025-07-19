package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOfficialQuestionRepository extends JpaRepository<OfficialQuestion, Long> {

    Page<OfficialQuestion> findAllByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);
}
