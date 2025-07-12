package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.personalQuestion.PersonalQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPersonalQuestionRepository extends JpaRepository<PersonalQuestion, Long> {
}
