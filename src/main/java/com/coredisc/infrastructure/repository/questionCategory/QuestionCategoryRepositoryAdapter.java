package com.coredisc.infrastructure.repository.questionCategory;

import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import com.coredisc.domain.mapping.questionCategory.QuestionCategoryRepository;
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
}
