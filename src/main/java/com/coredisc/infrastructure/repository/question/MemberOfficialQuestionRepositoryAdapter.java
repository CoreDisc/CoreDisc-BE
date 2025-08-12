package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestionRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.infrastructure.repository.question.querydsl.QueryMemberOfficialQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberOfficialQuestionRepositoryAdapter implements MemberOfficialQuestionRepository {

    private final JpaMemberOfficialQuestionRepository jpaMemberOfficialQuestionRepository;
    private final QueryMemberOfficialQuestionRepository queryMemberOfficialQuestionRepository;

    @Override
    public MemberOfficialQuestion save(MemberOfficialQuestion memberOfficialQuestion) {
        return jpaMemberOfficialQuestionRepository.save(memberOfficialQuestion);
    }

    @Override
    public Optional<MemberOfficialQuestion> findByMemberAndId(Member member, Long id) {
        return jpaMemberOfficialQuestionRepository.findByMemberAndId(member, id);
    }

    @Override
    public void delete(MemberOfficialQuestion memberOfficialQuestion) {
        jpaMemberOfficialQuestionRepository.delete(memberOfficialQuestion);
    }

    @Override
    public Optional<MemberOfficialQuestion> findByMemberAndOfficialQuestion(Member member, OfficialQuestion officialQuestion) {
        return jpaMemberOfficialQuestionRepository.findByMemberAndOfficialQuestion(member, officialQuestion);
    }

    @Override
    public long countByOfficialQuestion(OfficialQuestion officialQuestion) {
        return jpaMemberOfficialQuestionRepository.countByOfficialQuestion(officialQuestion);
    }

    @Override
    public List<MemberOfficialQuestion> findFavoritesByMember(Member member, Long cursorId, int size) {
        return queryMemberOfficialQuestionRepository.findFavoritesByMember(member, cursorId, size);
    }

    @Override
    public List<MemberOfficialQuestion> findAllByMemberAndCursor(Member member, Long cursorId, int pageSize) {
        return queryMemberOfficialQuestionRepository.findAllByMemberAndCursor(member, cursorId, pageSize);
    }

    @Override
    public List<MemberOfficialQuestion> findByMemberAndCategoryAndCursor(Member member, Category category, Long cursorId, int pageSize) {
        return queryMemberOfficialQuestionRepository.findByMemberAndCategoryAndCursor(member, category, cursorId, pageSize);
    }

    @Override
    public boolean existsByMemberIdAndOfficialQuestionIdAndIsFavoriteTrue(Long memberId, Long officialQuestionId) {
        return jpaMemberOfficialQuestionRepository.existsByMemberIdAndOfficialQuestionIdAndIsFavoriteTrue(memberId, officialQuestionId);
    }

    @Override
    public boolean existsByMemberIdAndOfficialQuestionId(Long memberId, Long officialQuestionId) {
        return jpaMemberOfficialQuestionRepository.existsByMemberIdAndOfficialQuestionId(memberId, officialQuestionId);
    }
}
