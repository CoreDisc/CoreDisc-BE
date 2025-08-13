package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.mapping.memberOfficialQuestion.QMemberOfficialQuestion;
import com.coredisc.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryMemberOfficialQuestionRepositoryImpl implements QueryMemberOfficialQuestionRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QMemberOfficialQuestion qMemberOfficialQuestion = QMemberOfficialQuestion.memberOfficialQuestion;
    

    @Override
    public List<MemberOfficialQuestion> findAllByMemberAndCursor(Member member, Long cursorId, int pageSize) {
        return jpaQueryFactory
                .selectFrom(qMemberOfficialQuestion)
                .where(
                        qMemberOfficialQuestion.member.eq(member),
                        cursorId != null ? qMemberOfficialQuestion.officialQuestion.id.lt(cursorId) : null
                )
                .orderBy(qMemberOfficialQuestion.officialQuestion.id.desc())
                .limit(pageSize + 1)
                .fetch();
    }

    @Override
    public List<MemberOfficialQuestion> findByMemberAndCategoryAndCursor(Member member, Category category, Long cursorId, int pageSize) {
        return jpaQueryFactory
                .selectFrom(qMemberOfficialQuestion)
                .where(
                        qMemberOfficialQuestion.member.eq(member),
                        qMemberOfficialQuestion.officialQuestion.questionCategoryList.any().category.eq(category),
                        cursorId != null ? qMemberOfficialQuestion.officialQuestion.id.lt(cursorId) : null
                )
                .orderBy(qMemberOfficialQuestion.officialQuestion.id.desc())
                .limit(pageSize + 1)
                .fetch();
    }

}
