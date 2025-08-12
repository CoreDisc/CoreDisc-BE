package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaMemberOfficialQuestionRepository extends JpaRepository<MemberOfficialQuestion, Long> {

    Optional<MemberOfficialQuestion> findByMemberAndId(Member member, Long id);

    Optional<MemberOfficialQuestion> findByMemberAndOfficialQuestion(Member member, OfficialQuestion officialQuestion);

    long countByOfficialQuestion(OfficialQuestion officialQuestion);

    boolean existsByMemberIdAndOfficialQuestionIdAndIsFavoriteTrue(Long memberId, Long officialQuestionId);

    boolean existsByMemberIdAndOfficialQuestionId(Long memberId, Long officialQuestionId);
}
