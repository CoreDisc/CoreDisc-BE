package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.officialQuestion.OfficialQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOfficialQuestionRepository extends JpaRepository<OfficialQuestion, Long> {
}
