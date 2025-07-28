package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.member.Member;

import java.util.List;

public interface QueryMemberOfficialQuestionRepository {

    List<MemberOfficialQuestion> findFavoritesByMember(Member member, Long cursorId, int size);

    List<MemberOfficialQuestion> findAllByMemberAndCursor(Member member, Long cursorId, int pageSize);

    List<MemberOfficialQuestion> findByMemberAndCategoryAndCursor(Member member, Category category, Long cursorId, int pageSize);

}
