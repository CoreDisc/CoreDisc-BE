package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberOfficialQuestionRepositoryAdapter implements MemberOfficialQuestionRepository {

    private final JpaMemberOfficialQuestionRepository jpaMemberOfficialQuestionRepository;

    @Override
    public MemberOfficialQuestion save(MemberOfficialQuestion memberOfficialQuestion) {
        return jpaMemberOfficialQuestionRepository.save(memberOfficialQuestion);
    }
}
