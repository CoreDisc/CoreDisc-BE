package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.officialQuestion.OfficialQuestionRepository;
import com.coredisc.infrastructure.repository.question.querydsl.QueryOfficialQuestionRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OfficialQuestionRepositoryAdapter implements OfficialQuestionRepository {

    private final JpaOfficialQuestionRepository jpaOfficialQuestionRepository;
    private final QueryOfficialQuestionRepository queryOfficialQuestionRepository;

    @Override
    public OfficialQuestion save(OfficialQuestion officialQuestion) {
        return jpaOfficialQuestionRepository.save(officialQuestion);
    }

    @Override
    public List<OfficialQuestion> findFavoritesByMember(Member member, Long cursorId, int pageSize) {
        return queryOfficialQuestionRepository.findFavoritesByMember(member, cursorId, pageSize);
    }

    @Override
    public List<OfficialQuestion> findAllByMemberAndCursor(Member member, Long cursorId, int pageSize){
        return queryOfficialQuestionRepository.findAllByMemberAndCursor(member, cursorId, pageSize);
    }

    @Override
    public List<OfficialQuestion> findAllByMemberAndCategory(Member member, Category category, Long cursorId, int pageSize) {
        return queryOfficialQuestionRepository.findAllByMemberAndCategory(member, category, cursorId, pageSize);
    }

    @Override
    public Long countOfficialQuestionByMember(Member member){
        return jpaOfficialQuestionRepository.countOfficialQuestionByMember(member);
    }

    @Override
    public Optional<OfficialQuestion> findById(Long id) {
        return jpaOfficialQuestionRepository.findById(id);
    }

    @Override
    public List<Tuple> findTop5PopularQuestionsThisWeek(LocalDate startOfWeek, LocalDate endOfWeek) {
        return queryOfficialQuestionRepository.findTop5PopularQuestionsThisWeek(startOfWeek, endOfWeek);
    }

    @Override
    public Boolean findIsFavoriteByIdAndMember(Long id, Member member) {
        return jpaOfficialQuestionRepository.findIsFavoriteByIdAndMember(id, member);
    }

}
