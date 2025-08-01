package com.coredisc.common.converter;

import com.coredisc.domain.common.enums.QuestionType;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public static QuestionResponseDTO.MySharedQuestionPreviewResultDTO toMySharedQuestionPreviewResultDTO(OfficialQuestion question, long sharedCount) {
        List<CategoryResponseDTO.CategoryInfoDTO> categories = question.getQuestionCategoryList().stream()
                .map(qc -> CategoryResponseDTO.CategoryInfoDTO.builder()
                        .categoryId(qc.getCategory().getId())
                        .categoryName(qc.getCategory().getName())
                        .build())
                .toList();

        return QuestionResponseDTO.MySharedQuestionPreviewResultDTO.builder()
                .id(question.getId())
                .categories(categories)
                .question(question.getContents())
                .sharedCount(sharedCount)
                .createdAt(question.getCreatedAt())
                .build();
    }

    public static QuestionResponseDTO.MySharedQuestionResultDTO toMySharedQuestionResultDTO(OfficialQuestion question, long sharedCount) {
        List<CategoryResponseDTO.CategoryInfoDTO> categories = question.getQuestionCategoryList().stream()
                .map(qc -> CategoryResponseDTO.CategoryInfoDTO.builder()
                        .categoryId(qc.getCategory().getId())
                        .categoryName(qc.getCategory().getName())
                        .build())
                .toList();

        return QuestionResponseDTO.MySharedQuestionResultDTO.builder()
                .id(question.getId())
                .categories(categories)
                .question(question.getContents())
                .sharedCount(sharedCount)
                .isFavorite(question.getIsFavorite())
                .createdAt(question.getCreatedAt())
                .build();
    }

    public static TodayQuestion toFixedTodayQuestionByOfficial(QuestionRequestDTO.SaveFixedTodayQuestionDTO request, QuestionType questionType, OfficialQuestion officialQuestion, Member member){

        return TodayQuestion.builder()
                .selectedDate(LocalDate.now())
                .questionType(questionType)
                .questionOrder(request.getQuestionOrder())
                .member(member)
                .officialQuestion(officialQuestion)
                .build();
    }

    public static TodayQuestion toFixedTodayQuestionByPersonal(QuestionRequestDTO.SaveFixedTodayQuestionDTO request, QuestionType questionType, PersonalQuestion personalQuestion, Member member){

        return TodayQuestion.builder()
                .selectedDate(LocalDate.now())
                .questionType(questionType)
                .questionOrder(request.getQuestionOrder())
                .member(member)
                .personalQuestion(personalQuestion)
                .build();
    }

    public static QuestionResponseDTO.SaveFixedTodayQuestionResultDTO toSaveFixedTodayQuestionResultDTO(TodayQuestion todayQuestion) {

        return QuestionResponseDTO.SaveFixedTodayQuestionResultDTO.builder()
                .id(todayQuestion.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static TodayQuestion toRandomTodayQuestionByOfficial(QuestionType questionType, OfficialQuestion officialQuestion, Member member){

        return TodayQuestion.builder()
                .selectedDate(LocalDate.now())
                .questionType(questionType)
                .questionOrder(4)
                .member(member)
                .officialQuestion(officialQuestion)
                .build();
    }

    public static TodayQuestion toRandomTodayQuestionByPersonal(QuestionType questionType, PersonalQuestion personalQuestion, Member member){

        return TodayQuestion.builder()
                .selectedDate(LocalDate.now())
                .questionType(questionType)
                .questionOrder(4)
                .member(member)
                .personalQuestion(personalQuestion)
                .build();
    }

    public static QuestionResponseDTO.SaveRandomTodayQuestionResultDTO toSaveRandomTodayQuestionResultDTO(TodayQuestion todayQuestion) {

        return QuestionResponseDTO.SaveRandomTodayQuestionResultDTO.builder()
                .id(todayQuestion.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static QuestionResponseDTO.SelectedTodayQuestionResultDTO toSelectedTodayQuestionResultDTO(Optional<TodayQuestion> todayQuestion, int questionOrder) {
        return todayQuestion
                .map(q -> QuestionResponseDTO.SelectedTodayQuestionResultDTO.builder()
                        .id(q.getId())
                        .questionOrder(questionOrder)
                        .question(q.getQuestionContent())
                        .questionType(q.getQuestionType())
                        .build())
                .orElseGet(() -> QuestionResponseDTO.SelectedTodayQuestionResultDTO.builder()
                        .id(null)
                        .questionOrder(questionOrder)
                        .question(null)
                        .questionType(null)
                        .build());
    }

    public static MemberOfficialQuestion toMemberOfficialQuestion(Member member, OfficialQuestion officialQuestion){

        return MemberOfficialQuestion.builder()
                .officialQuestion(officialQuestion)
                .member(member)
                .build();
    }

    public static QuestionResponseDTO.SaveMemberOfficialQuestionResultDTO toSaveMemberOfficialQuestionResultDTO(MemberOfficialQuestion memberOfficialQuestion) {

        return QuestionResponseDTO.SaveMemberOfficialQuestionResultDTO.builder()
                .id(memberOfficialQuestion.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static QuestionResponseDTO.SavedSharedQuestionResultDTO toSavedSharedQuestionResultDTO(MemberOfficialQuestion memberOfficialQuestion, long sharedCount) {
        OfficialQuestion officialQuestion = memberOfficialQuestion.getOfficialQuestion();

        List<CategoryResponseDTO.CategoryInfoDTO> categories = officialQuestion.getQuestionCategoryList().stream()
                .map(qc -> CategoryResponseDTO.CategoryInfoDTO.builder()
                        .categoryId(qc.getCategory().getId())
                        .categoryName(qc.getCategory().getName())
                        .build())
                .toList();

        return QuestionResponseDTO.SavedSharedQuestionResultDTO.builder()
                .id(officialQuestion.getId())
                .question(officialQuestion.getContents())
                .categories(categories)
                .sharedCount(sharedCount)
                .isFavorite(memberOfficialQuestion.getIsFavorite())
                .createdAt(officialQuestion.getCreatedAt())
                .build();
    }

    public static QuestionResponseDTO.PopularQuestionListResultDTO toPopularQuestionListResultDTO(List<Tuple> questionTuple, LocalDate startDate, LocalDate endDate) {

        List<QuestionResponseDTO.PopularQuestionResultDTO> questionList = questionTuple.stream()
                .map(tuple -> {
                    OfficialQuestion question = tuple.get(0, OfficialQuestion.class);
                    String username = tuple.get(1, String.class);
                    String contents = tuple.get(2, String.class);
                    Long sharedCount = tuple.get(3, Long.class);

                    return QuestionResponseDTO.PopularQuestionResultDTO.builder()
                            .id(question.getId())
                            .username(username)
                            .question(contents)
                            .sharedCount(sharedCount)
                            .build();
                })
                .toList();

        return QuestionResponseDTO.PopularQuestionListResultDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .popularQuestionList(questionList)
                .build();
    }

    public static QuestionResponseDTO.UpdateSavedSharedQuestionFavoriteStatusResultDTO toUpdateSavedSharedQuestionFavoriteStatusResultDTO(MemberOfficialQuestion memberOfficialQuestion) {

        return QuestionResponseDTO.UpdateSavedSharedQuestionFavoriteStatusResultDTO.builder()
                .id(memberOfficialQuestion.getId())
                .createdAt(memberOfficialQuestion.getCreatedAt())
                .build();
    }

    public static QuestionResponseDTO.UpdateMySharedQuestionFavoriteStatusResultDTO toUpdateMySharedQuestionFavoriteStatusResultDTO(OfficialQuestion officialQuestion) {

        return QuestionResponseDTO.UpdateMySharedQuestionFavoriteStatusResultDTO.builder()
                .id(officialQuestion.getId())
                .createdAt(officialQuestion.getCreatedAt())
                .build();
    }
}
