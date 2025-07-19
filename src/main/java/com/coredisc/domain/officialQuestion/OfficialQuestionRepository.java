package com.coredisc.domain.officialQuestion;


import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfficialQuestionRepository {

    OfficialQuestion save(OfficialQuestion officialQuestion);

    Page<OfficialQuestion> findAllByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);
}
