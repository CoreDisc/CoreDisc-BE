package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.mapping.questionCategory.QQuestionCategory;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.officialQuestion.QOfficialQuestion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryOfficialQuestionRepositoryImpl implements QueryOfficialQuestionRepository{

    private final JPAQueryFactory jpaQueryFactory;

    private final QOfficialQuestion qOfficialQuestion = QOfficialQuestion.officialQuestion;
    private final QQuestionCategory qQuestionCategory = QQuestionCategory.questionCategory;

    @Override
    public Page<OfficialQuestion> findAllByMemberAndCategory(Member member, Category category, Pageable pageable) {

        List<OfficialQuestion> sharedQuestionList = jpaQueryFactory
                .selectFrom(qOfficialQuestion)
                .join(qOfficialQuestion.questionCategoryList, qQuestionCategory)
                .where(
                        qOfficialQuestion.member.eq(member),
                        qQuestionCategory.category.eq(category)
                )
                .orderBy(qOfficialQuestion.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = jpaQueryFactory
                .select(qOfficialQuestion.count())
                .from(qOfficialQuestion)
                .join(qOfficialQuestion.questionCategoryList, qQuestionCategory)
                .where(
                        qOfficialQuestion.member.eq(member),
                        qQuestionCategory.category.eq(category)
                )
                .fetchOne();

        return new PageImpl<>(sharedQuestionList, pageable, totalCount != null ? totalCount : 0);
    }
}
