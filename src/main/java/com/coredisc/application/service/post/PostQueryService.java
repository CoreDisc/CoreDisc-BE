package com.coredisc.application.service.post;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.post.PostResponseDTO;

import java.time.LocalDate;

public interface PostQueryService {

    /**
     * 임시저장된 게시글 불러오기 (TEMP 상태)
     * 오늘의 질문에 대한 답변만 해당
     *
     * @param member       현재 사용자
     * @param selectedDate 선택한 날짜 (오늘 기준)
     * @return 임시저장된 게시글 정보
     */
    PostResponseDTO.TempAnswerPostDto getTempPost(Member member , LocalDate selectedDate);
}
