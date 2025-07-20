package com.coredisc.common.converter;

import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static QuestionResponseDTO.BasicQuestionListResultDTO toBasicQuestionListResultDTO(Page<QuestionResponseDTO.BasicQuestionResultDTO> basicQuestionList) {

        return QuestionResponseDTO.BasicQuestionListResultDTO.builder()
                .basicQuestionList(basicQuestionList.getContent())
                .listSize(basicQuestionList.getNumberOfElements())
                .totalPage(basicQuestionList.getTotalPages())
                .totalElements(basicQuestionList.getTotalElements())
                .isFirst(basicQuestionList.isFirst())
                .isLast(basicQuestionList.isLast())
                .build();
    }

    public static List<QuestionResponseDTO.MySharedQuestionResultDTO> toMySharedQuestionResultDTOList(List<OfficialQuestion> officialQuestions) {
        return officialQuestions.stream()
                .map(officialQuestion -> {
                    List<CategoryResponseDTO.CategoryInfoDTO> categories = officialQuestion.getQuestionCategoryList().stream()
                            .map(qc -> CategoryResponseDTO.CategoryInfoDTO.builder()
                                    .categoryId(qc.getCategory().getId())
                                    .categoryName(qc.getCategory().getName())
                                    .build())
                            .collect(Collectors.toList());

                    return QuestionResponseDTO.MySharedQuestionResultDTO.builder()
                            .id(officialQuestion.getId())
                            .categories(categories)
                            .question(officialQuestion.getContents())
                            .sharedCount(0L) // TODO: 공유 횟수 적용
                            .createdAt(officialQuestion.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public static Page<QuestionResponseDTO.MySharedQuestionResultDTO> toMySharedQuestionResultDTOPage(Page<OfficialQuestion> officialQuestionList, Pageable pageable) {
        List<QuestionResponseDTO.MySharedQuestionResultDTO> mySharedQuestiondtoList = toMySharedQuestionResultDTOList(officialQuestionList.getContent());
        return new org.springframework.data.domain.PageImpl<>(mySharedQuestiondtoList, pageable, officialQuestionList.getTotalElements());
    }

    public static QuestionResponseDTO.MySharedQuestionListResultDTO toMySharedQuestionListResultDTO(Page<QuestionResponseDTO.MySharedQuestionResultDTO> mySharedQuestionList, Long totalMySharedQuestionCnt) {
        return QuestionResponseDTO.MySharedQuestionListResultDTO.builder()
                .mySharedQuestionCnt(totalMySharedQuestionCnt)
                .mySharedQuestionList(mySharedQuestionList.getContent())
                .listSize(mySharedQuestionList.getNumberOfElements())
                .totalPage(mySharedQuestionList.getTotalPages())
                .totalElements(mySharedQuestionList.getTotalElements())
                .isFirst(mySharedQuestionList.isFirst())
                .isLast(mySharedQuestionList.isLast())
                .build();
    }
}
