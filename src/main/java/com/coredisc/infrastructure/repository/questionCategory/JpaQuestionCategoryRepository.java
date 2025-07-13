package com.coredisc.infrastructure.repository.questionCategory;

import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
}
