package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.personalQuestion.PersonalQuestionRepository;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonalQuestionRepositoryAdapter implements PersonalQuestionRepository {

    private final JpaPersonalQuestionRepository jpaPersonalQuestionRepository;

    @Override
    public PersonalQuestion save(PersonalQuestion personalQuestion) {
        return jpaPersonalQuestionRepository.save(personalQuestion);
    }

    @Override
    public Optional<PersonalQuestion> findById(Long id) {
        return jpaPersonalQuestionRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaPersonalQuestionRepository.deleteById(id);
    }
}
