package com.coredisc.application.service.question;

import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;

public interface QuestionCommandService {

    // 내가 작성한 질문 저장
    PersonalQuestion savePersonalQuestion(QuestionRequestDTO.SavePersonalQuestionDTO request, Member member);

    // 내가 작성한 질문 공유
    OfficialQuestion saveOfficialQuestion(QuestionRequestDTO.SaveOfficialQuestionDTO request, Member member);

    // 고정 질문 선택
    TodayQuestion saveFixedTodayQuestion(QuestionRequestDTO.SaveFixedTodayQuestionDTO request, Member member);


    // 랜덤 질문 선택
    TodayQuestion saveRandomTodayQuestion(QuestionRequestDTO.SaveRandomTodayQuestionDTO request, Member member);

    // 사용자가 작성하여 저장했던 질문 수정
    PersonalQuestion updatePersonalQuestion(Member member, Long questionId, QuestionRequestDTO.SavePersonalQuestionDTO request);

    // 사용자가 작성하여 저장했던 질문 삭제
    void deletePersonalQuestion(Member member, Long questionId);

    // 타사용자가 작성한 공유 질문 저장
    MemberOfficialQuestion saveMemberOfficialQuestion(Member member, Long questionId, QuestionRequestDTO.SaveMemberOfficialQuestionDTO request);

    // 저장헀던 공유 질문을 삭제
    void deleteMemberOfficialQuestion(Member member, Long savedId);
}
