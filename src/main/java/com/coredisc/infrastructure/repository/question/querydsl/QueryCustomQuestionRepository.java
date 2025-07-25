package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;

import java.time.LocalDateTime;

public interface QueryCustomQuestionRepository {
    CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByCategories (
            Long categoryId,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize
    );
}
