package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberOfficialQuestionRepository extends JpaRepository<MemberOfficialQuestion, Long> {
}
