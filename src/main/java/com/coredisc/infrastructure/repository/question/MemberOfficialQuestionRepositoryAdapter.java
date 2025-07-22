package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestionRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberOfficialQuestionRepositoryAdapter implements MemberOfficialQuestionRepository {

    private final JpaMemberOfficialQuestionRepository jpaMemberOfficialQuestionRepository;

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

}
