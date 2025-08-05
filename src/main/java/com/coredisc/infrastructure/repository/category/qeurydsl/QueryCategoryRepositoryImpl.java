package com.coredisc.infrastructure.repository.category.qeurydsl;

import com.coredisc.domain.category.QCategory;
import com.coredisc.domain.mapping.questionCategory.QQuestionCategory;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.QOfficialQuestion;
import com.coredisc.domain.personalQuestion.QPersonalQuestion;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryCategoryRepositoryImpl implements QueryCategoryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CategoryResponseDTO.CategoryDTO> findCategoryList(Member member) {
        QCategory qCategory = QCategory.category;
        QQuestionCategory qQuestionCategory = QQuestionCategory.questionCategory;
        QPersonalQuestion personal = QPersonalQuestion.personalQuestion;
        QOfficialQuestion official = QOfficialQuestion.officialQuestion;

        return jpaQueryFactory
                .select(Projections.constructor(
                        CategoryResponseDTO.CategoryDTO.class,
                        qCategory.id,
                        qCategory.name,
                        personal.id.count().add(official.id.count())
                ))
                .from(qCategory)
                .leftJoin(qQuestionCategory).on(qQuestionCategory.category.eq(qCategory))
                .leftJoin(qQuestionCategory.personalQuestion, personal)
                .on(personal.member.eq(member))
                .leftJoin(qQuestionCategory.officialQuestion, official) 
                .groupBy(qCategory.id)
                .fetch();
    }

    @Override
    public List<CategoryResponseDTO.CategoryDTO> findCategoryListByKeyword(Member member, String keyword) {
        QCategory category = QCategory.category;
        QQuestionCategory questionCategory = QQuestionCategory.questionCategory;
        QPersonalQuestion personal = QPersonalQuestion.personalQuestion;
        QOfficialQuestion official = QOfficialQuestion.officialQuestion;

        return jpaQueryFactory
                .select(Projections.constructor(
                        CategoryResponseDTO.CategoryDTO.class,
                        category.id,
                        category.name,
                        new CaseBuilder()
                                .when(category.name.eq(keyword))
                                .then(1L)
                                .otherwise(
                                        new CaseBuilder()
                                                .when(
                                                        personal.isNotNull()
                                                                .and(personal.member.eq(member))
                                                                .and(personal.content.containsIgnoreCase(keyword))
                                                                .or(
                                                                        official.isNotNull()
                                                                                .and(official.contents.containsIgnoreCase(keyword))
                                                                )
                                                )
                                                .then(1L)
                                                .otherwise(0L)
                                )
                                .sum()
                ))
                .from(category)
                .leftJoin(questionCategory).on(questionCategory.category.eq(category))
                .leftJoin(questionCategory.personalQuestion, personal)
                .leftJoin(questionCategory.officialQuestion, official)
                .groupBy(category.id)
                .fetch();
    }

}
