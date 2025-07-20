package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryOfficialQuestionRepository {

    Page<OfficialQuestion> findAllByMemberAndCategory(Member member, Category category, Pageable pageable);
}
