package com.coredisc.application.service.question;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.QuestionCategoryConverter;
import com.coredisc.common.converter.QuestionConverter;
import com.coredisc.common.exception.handler.QuestionHandler;
import com.coredisc.domain.common.enums.QuestionType;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.category.Category;
import com.coredisc.domain.category.CategoryRepository;
import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import com.coredisc.domain.mapping.questionCategory.QuestionCategoryRepository;
import com.coredisc.domain.officialQuestion.OfficialQuestionRepository;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.personalQuestion.PersonalQuestionRepository;
import com.coredisc.domain.todayQuestion.TodayQuestionRepository;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final PersonalQuestionRepository personalQuestionRepository;
    private final OfficialQuestionRepository officialQuestionRepository;
    private final TodayQuestionRepository todayQuestionRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionCategoryRepository questionCategoryRepository;

    // 내가 작성한 질문 저장
    @Override
    @Transactional
    public PersonalQuestion savePersonalQuestion(QuestionRequestDTO.SavePersonalQuestionDTO request, Member member){

        PersonalQuestion newPersonalQuestion = personalQuestionRepository.save(
                QuestionConverter.toPersonalQuestion(request, member)
        );

        if (request.getCategoryIdList() != null) {
            for (Long categoryId : request.getCategoryIdList()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new QuestionHandler(ErrorStatus.CATEGORY_NOT_FOUND));

                QuestionCategory questionCategory = QuestionCategoryConverter.toQuestionCategoryByPersonalQuestion(category, newPersonalQuestion);

                questionCategoryRepository.save(questionCategory);
            }
        }

        return newPersonalQuestion;
    }

    // 내가 작성한 질문 공유
    @Override
    @Transactional
    public OfficialQuestion saveOfficialQuestion(QuestionRequestDTO.SaveOfficialQuestionDTO request, Member member){

        OfficialQuestion newOfficialQuestion = officialQuestionRepository.save(
                QuestionConverter.toOfficialQuestion(request, member)
        );

        if (request.getCategoryIdList() != null) {
            for (Long categoryId : request.getCategoryIdList()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new QuestionHandler(ErrorStatus.CATEGORY_NOT_FOUND));

                QuestionCategory questionCategory = QuestionCategoryConverter.toQuestionCategoryByOfficialQuestion(category, newOfficialQuestion);

                questionCategoryRepository.save(questionCategory);
            }
        }

        return newOfficialQuestion;
    }

    // 고정 질문 선택
    @Override
    @Transactional
    public TodayQuestion saveFixedTodayQuestion(QuestionRequestDTO.SaveFixedTodayQuestionDTO request, Member member) {

        // 이번 달에 해당 번호 이미 있는지 여부
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(
                member, request.getQuestionOrder(), startOfMonth, endOfMonth
        ).ifPresent(q -> {
            throw new QuestionHandler(ErrorStatus.DUPLICATE_FIXED_TODAY_QUESTION_ORDER);
        });

        TodayQuestion newTodayQuestion;

        if (request.getSelectedQuestionType().equals("DEFAULT")) {  // 기본 질문

            OfficialQuestion selectedQuestion = officialQuestionRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.BASIC_QUESTION_NOT_FOUND));
            newTodayQuestion = QuestionConverter.toFixedTodayQuestionByOfficial(request, QuestionType.FIXED, selectedQuestion, member);

        } else if (request.getSelectedQuestionType().equals("OFFICIAL")) {  // 공유 질문

            OfficialQuestion selectedQuestion = officialQuestionRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.OFFICIAL_QUESTION_NOT_FOUND));
            newTodayQuestion = QuestionConverter.toFixedTodayQuestionByOfficial(request, QuestionType.FIXED, selectedQuestion, member);

        } else if (request.getSelectedQuestionType().equals("PERSONAL")) {  // 저장 질문

            PersonalQuestion selectedQuestion = personalQuestionRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.PERSONAL_QUESTION_NOT_FOUND));
            newTodayQuestion = QuestionConverter.toFixedTodayQuestionByPersonal(request, QuestionType.FIXED, selectedQuestion, member);

        } else {    // 잘못된 질문 타입
            throw new QuestionHandler(ErrorStatus.QUESTION_TYPE_NOT_FOUND);
        }

        return todayQuestionRepository.save(newTodayQuestion);
    }

    // 랜덤 질문 선택
    @Override
    @Transactional
    public TodayQuestion saveRandomTodayQuestion(QuestionRequestDTO.SaveRandomTodayQuestionDTO request, Member member) {

        // 오늘 해당 번호 이미 있는지 여부
        LocalDate today = LocalDate.now();
        int questionOrder = 4;

        todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDate(member, questionOrder, today)
                .ifPresent(q -> {
            throw new QuestionHandler(ErrorStatus.DUPLICATE_RANDOM_TODAY_QUESTION_ORDER);
        });

        TodayQuestion newTodayQuestion;

        if (request.getSelectedQuestionType().equals("DEFAULT")) {  // 기본 질문

            OfficialQuestion selectedQuestion = officialQuestionRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.BASIC_QUESTION_NOT_FOUND));
            newTodayQuestion = QuestionConverter.toRandomTodayQuestionByOfficial(QuestionType.RANDOM, selectedQuestion, member);

        } else if (request.getSelectedQuestionType().equals("OFFICIAL")) {  // 공유 질문

            OfficialQuestion selectedQuestion = officialQuestionRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.OFFICIAL_QUESTION_NOT_FOUND));
            newTodayQuestion = QuestionConverter.toRandomTodayQuestionByOfficial(QuestionType.RANDOM, selectedQuestion, member);

        } else if (request.getSelectedQuestionType().equals("PERSONAL")) {  // 저장 질문

            PersonalQuestion selectedQuestion = personalQuestionRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.PERSONAL_QUESTION_NOT_FOUND));
            newTodayQuestion = QuestionConverter.toRandomTodayQuestionByPersonal(QuestionType.RANDOM, selectedQuestion, member);

        } else {    // 잘못된 질문 타입
            throw new QuestionHandler(ErrorStatus.QUESTION_TYPE_NOT_FOUND);
        }

        return todayQuestionRepository.save(newTodayQuestion);
    }

    // 사용자가 작성하여 저장했던 질문 수정
    @Override
    @Transactional
    public PersonalQuestion updatePersonalQuestion(Member member, Long questionId, QuestionRequestDTO.SavePersonalQuestionDTO request) {

        PersonalQuestion existPersonalQuestion = personalQuestionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.PERSONAL_QUESTION_NOT_FOUND));

        // 작성자 일치 여부
        if (!existPersonalQuestion.getMember().equals(member))
            throw new QuestionHandler(ErrorStatus.UNAUTHORIZED_PERSONAL_QUESTION_ACCESS);

        // 질문 내용 수정
        existPersonalQuestion.updatePersonalQuestion(request.getQuestion());

        // 기존 선택된 카테고리 삭제
        questionCategoryRepository.deleteByPersonalQuestion(existPersonalQuestion);

        // 카테고리 수정
        if (request.getCategoryIdList() != null) {
            for (Long categoryId : request.getCategoryIdList()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new QuestionHandler(ErrorStatus.CATEGORY_NOT_FOUND));

                QuestionCategory questionCategory = QuestionCategoryConverter.toQuestionCategoryByPersonalQuestion(category, existPersonalQuestion);

                questionCategoryRepository.save(questionCategory);
            }
        }

        return existPersonalQuestion;
    }


    // 사용자가 작성하여 저장했던 질문 삭제
    @Override
    @Transactional
    public void deletePersonalQuestion(Member member, Long questionId) {

        PersonalQuestion existPersonalQuestion = personalQuestionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.PERSONAL_QUESTION_NOT_FOUND));

        // 작성자 일치 여부
        if (!existPersonalQuestion.getMember().equals(member))
            throw new QuestionHandler(ErrorStatus.UNAUTHORIZED_PERSONAL_QUESTION_ACCESS);

        // 고정 또는 랜덤 질문으로 사용되었으면 삭제 불가
        if (todayQuestionRepository.existsByPersonalQuestion(existPersonalQuestion)) {
            throw new QuestionHandler(ErrorStatus.PERSONAL_QUESTION_USED_IN_TODAY_QUESTION);
        }
        
        // 선택된 카테고리 삭제
        questionCategoryRepository.deleteByPersonalQuestion(existPersonalQuestion);

        personalQuestionRepository.deleteById(questionId);
    }
}
