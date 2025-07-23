package com.coredisc.infrastructure.repository.questionCategory;

import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import com.coredisc.domain.mapping.questionCategory.QuestionCategoryRepository;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionCategoryRepositoryAdapter implements QuestionCategoryRepository {

    private final JpaQuestionCategoryRepository jpaQuestionCategoryRepository;

    @Override
    public QuestionCategory save(QuestionCategory questionCategory) {
        return jpaQuestionCategoryRepository.save(questionCategory);
    }

    @Override
    public void deleteByPersonalQuestion(PersonalQuestion personalQuestion) {
        jpaQuestionCategoryRepository.deleteByPersonalQuestion(personalQuestion);
    }
}
