package com.coredisc.application.service.question;

import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;

public interface QuestionCommandService {

    // 내가 작성한 질문 저장
    PersonalQuestion savePersonalQuestion(QuestionRequestDTO.SavePersonalQuestionDTO request, Member member);

    // 내가 작성한 질문 공유
    OfficialQuestion saveOfficialQuestion(QuestionRequestDTO.SaveOfficialQuestionDTO request, Member member);
}
