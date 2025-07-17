package com.coredisc.infrastructure.repository.category.qeurydsl;

import com.coredisc.domain.category.QCategory;
import com.coredisc.domain.mapping.questionCategory.QQuestionCategory;
import com.coredisc.domain.officialQuestion.QOfficialQuestion;
import com.coredisc.domain.personalQuestion.QPersonalQuestion;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryCategoryRepositoryImpl implements QueryCategoryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CategoryResponseDTO.CategoryDTO> findCategoryList() {
        QCategory qCategory = QCategory.category;
        QQuestionCategory qQuestionCategory = QQuestionCategory.questionCategory;
        QOfficialQuestion qOfficialQuestion = QOfficialQuestion.officialQuestion;
        QPersonalQuestion qPersonalQuestion = QPersonalQuestion.personalQuestion;

        NumberExpression<Long> conditionalCount = new CaseBuilder()
                .when(
                        qOfficialQuestion.isShared.eq(false)
                                .or(qQuestionCategory.personalQuestion.isNotNull())
                )
                .then(1L)
                .otherwise(0L)
                .sum();

        return jpaQueryFactory
                .select(Projections.constructor(
                        CategoryResponseDTO.CategoryDTO.class,
                        qCategory.id,
                        qCategory.name,
                        conditionalCount
                ))
                .from(qCategory)
                .leftJoin(qQuestionCategory).on(qQuestionCategory.category.eq(qCategory))
                .leftJoin(qQuestionCategory.officialQuestion, qOfficialQuestion)
                .leftJoin(qQuestionCategory.personalQuestion, qPersonalQuestion)
                .groupBy(qCategory.id)
                .fetch();

    }
}
