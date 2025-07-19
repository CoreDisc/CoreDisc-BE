package com.coredisc.domain.personalQuestion;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonalQuestionRepository {

    PersonalQuestion save(PersonalQuestion personalQuestion);

    Page<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByCategories(Member member, Category category, Pageable pageable);

    Page<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByKeyword(Member member, String keyword, Pageable pageable);
}
