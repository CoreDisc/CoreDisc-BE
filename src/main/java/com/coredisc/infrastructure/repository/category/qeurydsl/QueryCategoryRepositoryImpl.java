package com.coredisc.infrastructure.repository.category.qeurydsl;

import com.coredisc.domain.category.QCategory;
import com.coredisc.domain.mapping.questionCategory.QQuestionCategory;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import com.querydsl.core.types.Projections;
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

        return jpaQueryFactory
                .select(Projections.constructor(
                        CategoryResponseDTO.CategoryDTO.class,
                        qCategory.id,
                        qCategory.name,
                        qQuestionCategory.id.count()
                ))
                .from(qCategory)
                .leftJoin(qQuestionCategory).on(qQuestionCategory.category.eq(qCategory))
                .groupBy(qCategory.id)
                .fetch();
    }
}
