package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface QueryPersonalQuestionRepository {

    Page<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByCategories(Member member, Category category, Pageable pageable);
}
