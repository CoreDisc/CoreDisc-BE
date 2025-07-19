package com.coredisc.domain.officialQuestion;


import com.coredisc.domain.category.Category;
import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfficialQuestionRepository {

    OfficialQuestion save(OfficialQuestion officialQuestion);

    Page<OfficialQuestion> findAllByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    Page<OfficialQuestion> findAllByMemberAndCategory(Member member, Category category, Pageable pageable);

    Long countOfficialQuestionByMember(Member member);
}
