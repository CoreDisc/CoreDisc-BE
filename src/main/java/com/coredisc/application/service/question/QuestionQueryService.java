package com.coredisc.application.service.question;


import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface QuestionQueryService {

    // 기본 질문 리스트 조회 (카테고리별)
    CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionList(
            Member member,
            Long categoryId,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize);

    // 기본 질문 검색 리스트 조회
    CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionSearchList(
            Member member,
            String keyword,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize);


    // 내가 발행한 공유질문 리스트 조회 (카테고리 필터링 포함)
    CursorDTO<QuestionResponseDTO.MySharedQuestionResultDTO> getMySharedQuestionList(Member member, Long categoryId, Boolean favorite, Long cursorId, int pageSize);

    // 선택한 고정&랜덤 질문 조회
    List<QuestionResponseDTO.SelectedTodayQuestionResultDTO> getMyTodayQuestion(Member member);

    // 저장한 공유 질문 목록 조회
    CursorDTO<QuestionResponseDTO.SavedSharedQuestionResultDTO> getSavedSharedQuestionList(
            Member member,
            Long categoryId,
            Boolean favorite,
            Long cursorId,
            int pageSize);

    // 인기 질문 목록 조회
    QuestionResponseDTO.PopularQuestionListResultDTO getPopularQuestionList();
}
