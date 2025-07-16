package com.coredisc.common.converter;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.personalQuestion.PersonalQuestion;

public class QuestionCategoryConverter {

    public static QuestionCategory toQuestionCategoryByPersonalQuestion(Category category, PersonalQuestion personalQuestion) {

        return QuestionCategory.builder()
                .category(category)
                .personalQuestion(personalQuestion)
                .build();
    }

    public static QuestionCategory toQuestionCategoryByOfficialQuestion(Category category, OfficialQuestion officialQuestion) {

        return QuestionCategory.builder()
                .category(category)
                .officialQuestion(officialQuestion)
                .build();
    }
}
