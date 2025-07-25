package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.category.QCategory;
import com.coredisc.domain.mapping.questionCategory.QQuestionCategory;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.QOfficialQuestion;
import com.coredisc.domain.personalQuestion.QPersonalQuestion;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class QueryPersonalQuestionRepositoryImpl implements QueryPersonalQuestionRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QOfficialQuestion qOfficialQuestion = QOfficialQuestion.officialQuestion;
    private final QPersonalQuestion qPersonalQuestion = QPersonalQuestion.personalQuestion;
    private final QQuestionCategory qQuestionCategory = QQuestionCategory.questionCategory;
    private final QCategory qCategory = QCategory.category;

    @Override
    public Page<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByKeyword(Member member, String keyword, Pageable pageable) {

        // 키워드와 일치하는 카테고리 검색
        Category matchedCategory = jpaQueryFactory
                .selectFrom(qCategory)
                .where(qCategory.name.eq(keyword))
                .fetchFirst();

        // 저장 질문 검색
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
                        qPersonalQuestion.member.eq(member)
                                .and(
                                        qPersonalQuestion.content.containsIgnoreCase(keyword)
                                                .or(matchedCategory != null ? qQuestionCategory.category.eq(matchedCategory) : Expressions.FALSE)
                                )
                )
                .distinct()
                .fetch();

        // 기본 질문 검색
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
                        qOfficialQuestion.isShared.isFalse()
                                .and(
                                        qOfficialQuestion.contents.containsIgnoreCase(keyword)
                                                .or(matchedCategory != null ? qQuestionCategory.category.eq(matchedCategory) : Expressions.FALSE)
                                )
                )
                .distinct()
                .fetch();


        List<QuestionResponseDTO.BasicQuestionResultDTO> mergedResults = Stream.concat(officialResults.stream(), personalResults.stream())
                .sorted(
                        Comparator.comparing(QuestionResponseDTO.BasicQuestionResultDTO::getId).reversed()
                                .thenComparing(dto -> dto.getQuestionType().equals("PERSONAL") ? 0 : 1)
                )
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());


        long total = officialResults.size() + personalResults.size();

        return new PageImpl<>(mergedResults, pageable, total);

    }
}
