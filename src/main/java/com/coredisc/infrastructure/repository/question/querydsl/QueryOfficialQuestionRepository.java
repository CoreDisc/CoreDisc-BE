package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.List;

public interface QueryOfficialQuestionRepository {

    List<OfficialQuestion> findFavoritesByMember(Member member, Long cusorId, int pageSize);

    List<OfficialQuestion> findAllByMemberAndCursor(Member member, Long cursorId, int pageSize);

    List<OfficialQuestion> findAllByMemberAndCategory(Member member, Category category, Long cursorId, int pageSize);

    List<Tuple> findTop5PopularQuestionsThisWeek(LocalDate startOfWeek, LocalDate endOfWeek);

}
