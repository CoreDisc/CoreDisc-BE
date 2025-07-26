package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;

import java.util.List;

public interface QueryOfficialQuestionRepository {

    List<OfficialQuestion> findAllByMemberAndCursor(Member member, Long cursorId, int pageSize);

    List<OfficialQuestion> findAllByMemberAndCategory(Member member, Category category, Long cursorId, int pageSize);
}
