package com.coredisc.domain.mapping.memberOfficialQuestion;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;

import java.util.List;
import java.util.Optional;

public interface MemberOfficialQuestionRepository {

    MemberOfficialQuestion save(MemberOfficialQuestion memberOfficialQuestion);

    Optional<MemberOfficialQuestion> findByMemberAndId(Member member, Long id);

    void delete(MemberOfficialQuestion memberOfficialQuestion);

    Optional<MemberOfficialQuestion> findByMemberAndOfficialQuestion(Member member, OfficialQuestion officialQuestion);

    long countByOfficialQuestion(OfficialQuestion officialQuestion);

    List<MemberOfficialQuestion> findFavoritesByMember(Member member, Long cursorId, int size);

    List<MemberOfficialQuestion> findAllByMemberAndCursor(Member member, Long cursorId, int pageSize);

    List<MemberOfficialQuestion> findByMemberAndCategoryAndCursor(Member member, Category category, Long cursorId, int pageSize);

    boolean existsByMemberIdAndOfficialQuestionIdAndIsFavoriteTrue(Long memberId, Long officialQuestionId);

    boolean existsByMemberIdAndOfficialQuestionId(Long memberId, Long officialQuestionId);

}
