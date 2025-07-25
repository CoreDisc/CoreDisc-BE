package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.coredisc.infrastructure.repository.question.querydsl.QueryCustomQuestionRepository;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class CustomQuestionRepositoryAdapter implements CustomQuestionRepository{

    private final QueryCustomQuestionRepository queryCustomQuestionRepository;

    @Override
    public CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByCategories (
            Long memberId,
            Long categoryId,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize
    ) {
        return queryCustomQuestionRepository.findBasicQuestionListByCategories(memberId, categoryId, cursorCreatedAt, cursorQuestionType, cursorId, pageSize);
    }

    @Override
    public CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByKeyword(
            Long memberId,
            String keyword,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize
    ) {
        return queryCustomQuestionRepository.findBasicQuestionListByKeyword(memberId, keyword, cursorCreatedAt, cursorQuestionType, cursorId, pageSize);
    }
}
