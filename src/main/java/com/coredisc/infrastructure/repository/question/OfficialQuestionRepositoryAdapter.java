package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.officialQuestion.OfficialQuestionRepository;
import com.coredisc.infrastructure.repository.question.querydsl.QueryOfficialQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public Page<OfficialQuestion> findAllByMemberOrderByCreatedAtDesc(Member member, Pageable pageable){
        return jpaOfficialQuestionRepository.findAllByMemberOrderByCreatedAtDesc(member, pageable);
    }

    @Override
    public Page<OfficialQuestion> findAllByMemberAndCategory(Member member, Category category, Pageable pageable) {
        return queryOfficialQuestionRepository.findAllByMemberAndCategory(member, category, pageable);
    }

    @Override
    public Long countOfficialQuestionByMember(Member member){
        return jpaOfficialQuestionRepository.countOfficialQuestionByMember(member);
    }
}
