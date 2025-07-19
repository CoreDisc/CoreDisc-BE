package com.coredisc.application.service.question;


import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionQueryService {

    // 기본 질문 리스트 조회 (카테고리별)
    Page<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionList(Member member, Long categoryId, Pageable pageable);

    // 기본 질문 검색 리스트 조회
    Page<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionSearchList(Member member, String keyword, Pageable pageable);


    // 내가 발행한 공유질문 리스트 조회
    Page<QuestionResponseDTO.MySharedQuestionResultDTO> getMySharedQuestionList(Member member, Pageable pageable);
}
