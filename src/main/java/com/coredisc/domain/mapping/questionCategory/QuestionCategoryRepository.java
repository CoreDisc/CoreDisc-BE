package com.coredisc.domain.mapping.questionCategory;

import com.coredisc.domain.personalQuestion.PersonalQuestion;

public interface QuestionCategoryRepository {

    QuestionCategory save(QuestionCategory questionCategory);

    void deleteByPersonalQuestion(PersonalQuestion personalQuestion);
}
