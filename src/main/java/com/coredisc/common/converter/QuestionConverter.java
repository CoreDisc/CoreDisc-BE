package com.coredisc.common.converter;

import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;

import java.time.LocalDateTime;

public class QuestionConverter {

    public static PersonalQuestion toPersonalQuestion(QuestionRequestDTO.SavePersonalQuestionDTO request, Member member){

        return PersonalQuestion.builder()
                .content(request.getQuestion())
                .member(member)
                .build();
    }

    public static QuestionResponseDTO.savePersonalQuestionResultDTO toSavePersonalQuestionResultDTO(PersonalQuestion personalQuestion) {

        return QuestionResponseDTO.savePersonalQuestionResultDTO.builder()
                .id(personalQuestion.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static OfficialQuestion toOfficialQuestion(QuestionRequestDTO.SaveOfficialQuestionDTO request, Member member){

        return OfficialQuestion.builder()
                .contents(request.getQuestion())
                .member(member)
                .build();
    }

    public static QuestionResponseDTO.saveOfficialQuestionResultDTO toSaveOfficialQuestionResultDTO(OfficialQuestion officialQuestion) {

        return QuestionResponseDTO.saveOfficialQuestionResultDTO.builder()
                .id(officialQuestion.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
