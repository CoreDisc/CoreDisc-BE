package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.personalQuestion.PersonalQuestionRepository;
import com.coredisc.infrastructure.repository.question.querydsl.QueryPersonalQuestionRepository;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonalQuestionRepositoryAdapter implements PersonalQuestionRepository {

    private final JpaPersonalQuestionRepository jpaPersonalQuestionRepository;
    private final QueryPersonalQuestionRepository queryPersonalQuestionRepository;

    @Override
    public PersonalQuestion save(PersonalQuestion personalQuestion) {
        return jpaPersonalQuestionRepository.save(personalQuestion);
    }

    @Override
    public Page<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByCategories(Member member, Category category, Pageable pageable) {
        return queryPersonalQuestionRepository.findBasicQuestionListByCategories(member, category, pageable);
    }
}
