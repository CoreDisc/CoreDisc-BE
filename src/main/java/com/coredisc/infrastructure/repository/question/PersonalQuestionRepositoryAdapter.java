package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.personalQuestion.PersonalQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PersonalQuestionRepositoryAdapter implements PersonalQuestionRepository {

    private final JpaPersonalQuestionRepository jpaPersonalQuestionRepository;

    @Override
    public PersonalQuestion save(PersonalQuestion personalQuestion) {
        return jpaPersonalQuestionRepository.save(personalQuestion);
    }
}
