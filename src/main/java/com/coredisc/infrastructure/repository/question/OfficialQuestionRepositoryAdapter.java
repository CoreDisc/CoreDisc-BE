package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.officialQuestion.OfficialQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OfficialQuestionRepositoryAdapter implements OfficialQuestionRepository {

    private final JpaOfficialQuestionRepository jpaOfficialQuestionRepository;

    @Override
    public OfficialQuestion save(OfficialQuestion officialQuestion) {
        return jpaOfficialQuestionRepository.save(officialQuestion);
    }
}
