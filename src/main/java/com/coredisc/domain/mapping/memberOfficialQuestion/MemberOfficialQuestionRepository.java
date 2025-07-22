package com.coredisc.domain.mapping.memberOfficialQuestion;

import com.coredisc.domain.member.Member;

import java.util.Optional;

public interface MemberOfficialQuestionRepository {

    MemberOfficialQuestion save(MemberOfficialQuestion memberOfficialQuestion);

    Optional<MemberOfficialQuestion> findByMemberAndId(Member member, Long id);

    void delete(MemberOfficialQuestion memberOfficialQuestion);
}
