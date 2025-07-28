package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.mapping.memberOfficialQuestion.QMemberOfficialQuestion;
import com.coredisc.domain.mapping.questionCategory.QQuestionCategory;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.officialQuestion.QOfficialQuestion;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryOfficialQuestionRepositoryImpl implements QueryOfficialQuestionRepository{

    private final JPAQueryFactory jpaQueryFactory;

    private final QOfficialQuestion qOfficialQuestion = QOfficialQuestion.officialQuestion;
    private final QQuestionCategory qQuestionCategory = QQuestionCategory.questionCategory;

    private final QMemberOfficialQuestion qMemberOfficialQuestion = QMemberOfficialQuestion.memberOfficialQuestion;

    public List<OfficialQuestion> findAllByMemberAndCursor(Member member, Long cursorId, int pageSize) {
        BooleanExpression cursorCondition = null;

        if (cursorId != null) {
            cursorCondition = qOfficialQuestion.id.lt(cursorId);
        }

        List<OfficialQuestion> officialQuestionList = jpaQueryFactory.selectFrom(qOfficialQuestion)
                .where(qOfficialQuestion.member.eq(member)
                        .and(cursorCondition))
                .orderBy(qOfficialQuestion.id.desc())
                .limit(pageSize + 1)
                .fetch();

        return officialQuestionList;
    }

    @Override
    public List<OfficialQuestion> findAllByMemberAndCategory(Member member, Category category, Long cursorId, int pageSize) {

        BooleanExpression cursorCondition = null;

        if (cursorId != null) {
            cursorCondition = qOfficialQuestion.id.lt(cursorId);
        }

        List<OfficialQuestion> officialQuestionList = jpaQueryFactory
                .selectFrom(qOfficialQuestion)
                .join(qOfficialQuestion.questionCategoryList, qQuestionCategory)
                .where(
                        qOfficialQuestion.member.eq(member),
                        qQuestionCategory.category.eq(category),
                        cursorCondition
                )
                .orderBy(qOfficialQuestion.id.desc())
                .limit(pageSize + 1)
                .fetch();

        return officialQuestionList;
    }

    @Override
    public List<Tuple> findTop5PopularQuestionsThisWeek(LocalDate startOfWeek, LocalDate endOfWeek) {

        return jpaQueryFactory
                .select(qMemberOfficialQuestion.officialQuestion, qMemberOfficialQuestion.officialQuestion.member.username, qMemberOfficialQuestion.officialQuestion.contents, qMemberOfficialQuestion.count())
                .from(qMemberOfficialQuestion)
                .where(qMemberOfficialQuestion.createdAt.between(startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX)))
                .groupBy(qMemberOfficialQuestion.officialQuestion)
                .orderBy(qMemberOfficialQuestion.count().desc())
                .limit(5)
                .fetch();
    }

}
