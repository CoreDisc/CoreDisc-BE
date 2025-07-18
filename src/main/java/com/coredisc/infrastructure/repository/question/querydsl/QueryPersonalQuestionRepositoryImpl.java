package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.mapping.questionCategory.QQuestionCategory;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.QOfficialQuestion;
import com.coredisc.domain.personalQuestion.QPersonalQuestion;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class QueryPersonalQuestionRepositoryImpl implements QueryPersonalQuestionRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByCategories(Member member, Category category, Pageable pageable) {

        QOfficialQuestion qOfficialQuestion = QOfficialQuestion.officialQuestion;
        QPersonalQuestion qPersonalQuestion = QPersonalQuestion.personalQuestion;
        QQuestionCategory qQuestionCategory = QQuestionCategory.questionCategory;

        // 저장 질문 조회
        List<QuestionResponseDTO.BasicQuestionResultDTO> personalResults = jpaQueryFactory
                .select(Projections.constructor(
                        QuestionResponseDTO.BasicQuestionResultDTO.class,
                        qPersonalQuestion.id,
                        Expressions.constant("PERSONAL"),
                        qPersonalQuestion.content
                ))
                .from(qPersonalQuestion)
                .join(qPersonalQuestion.questionCategoryList, qQuestionCategory)
                .where(
                        qPersonalQuestion.member.eq(member),
                        qQuestionCategory.category.eq(category)
                )
                .orderBy(qPersonalQuestion.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
        // 기본 질문 조회
        List<QuestionResponseDTO.BasicQuestionResultDTO> officialResults = jpaQueryFactory
                .select(Projections.constructor(
                        QuestionResponseDTO.BasicQuestionResultDTO.class,
                        qOfficialQuestion.id,
                        Expressions.constant("DEFAULT"),
                        qOfficialQuestion.contents
                ))
                .from(qOfficialQuestion)
                .join(qOfficialQuestion.questionCategoryList, qQuestionCategory)
                .where(
                        qQuestionCategory.category.eq(category),
                        qOfficialQuestion.isShared.isFalse()
                )
                .orderBy(qOfficialQuestion.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        List<QuestionResponseDTO.BasicQuestionResultDTO> basicQuestionList = Stream.concat(officialResults.stream(), personalResults.stream())
                .sorted(
                        Comparator.comparing(QuestionResponseDTO.BasicQuestionResultDTO::getId).reversed()
                                .thenComparing(dto -> dto.getQuestionType().equals("PERSONAL") ? 0 : 1)
                )
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());


        // 전체 개수 쿼리 수행 (공식 질문 수 + 개인 질문 수)
        Long personalCount = jpaQueryFactory
                .select(qPersonalQuestion.count())
                .from(qPersonalQuestion)
                .join(qPersonalQuestion.questionCategoryList, qQuestionCategory)
                .where(
                        qPersonalQuestion.member.eq(member),
                        qQuestionCategory.category.eq(category)
                )
                .fetchOne();

        Long officialCount = jpaQueryFactory
                .select(qOfficialQuestion.count())
                .from(qOfficialQuestion)
                .join(qOfficialQuestion.questionCategoryList, qQuestionCategory)
                .where(
                        qQuestionCategory.category.eq(category),
                        qOfficialQuestion.isShared.isFalse()
                )
                .fetchOne();

        long totalCount = (officialCount == null ? 0 : officialCount) + (personalCount == null ? 0 : personalCount);

        return new PageImpl<>(basicQuestionList, pageable, totalCount);
    }
}
