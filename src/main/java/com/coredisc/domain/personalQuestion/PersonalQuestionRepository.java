package com.coredisc.domain.personalQuestion;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PersonalQuestionRepository {

    PersonalQuestion save(PersonalQuestion personalQuestion);

    Optional<PersonalQuestion> findById(Long id);

    void deleteById(Long id);
}
