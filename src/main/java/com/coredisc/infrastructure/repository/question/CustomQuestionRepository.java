package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;

import java.time.LocalDateTime;

public interface CustomQuestionRepository {
    CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByCategories (
            Long memberId,
            Long categoryId,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize
    );

    CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByKeyword(
            Long memberId,
            Long categoryId,
            String keyword,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize
    );

    CursorDTO<MemberResponseDTO.MyHomeQuestionDTO> findMyHomeQuestionListByCategories (
            Long memberId,
            Long categoryId,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize
    );
}
