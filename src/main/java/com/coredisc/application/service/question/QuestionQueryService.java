package com.coredisc.application.service.question;


import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionQueryService {

    // 기본 질문 리스트 조회 (카테고리별)
    Page<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionList(Member member, Long categoryId, Pageable pageable);

    // 기본 질문 검색 리스트 조회
    Page<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionSearchList(Member member, String keyword, Pageable pageable);


    // 내가 발행한 공유질문 리스트 조회 (카테고리 필터링 포함)
    QuestionResponseDTO.MySharedQuestionListResultDTO getMySharedQuestionList(Member member, Long categoryId, Pageable pageable);

    // 선택한 고정&랜덤 질문 조회
    List<QuestionResponseDTO.SelectedTodayQuestionResultDTO> getMyTodayQuestion(Member member);
}
