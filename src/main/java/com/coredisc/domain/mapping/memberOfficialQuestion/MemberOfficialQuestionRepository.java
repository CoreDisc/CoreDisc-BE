package com.coredisc.domain.mapping.memberOfficialQuestion;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;

import java.util.Optional;

public interface MemberOfficialQuestionRepository {

    MemberOfficialQuestion save(MemberOfficialQuestion memberOfficialQuestion);

    Optional<MemberOfficialQuestion> findByMemberAndId(Member member, Long id);

    void delete(MemberOfficialQuestion memberOfficialQuestion);

    Optional<MemberOfficialQuestion> findByMemberAndOfficialQuestion(Member member, OfficialQuestion officialQuestion);

    long countByOfficialQuestion(OfficialQuestion officialQuestion);
}
