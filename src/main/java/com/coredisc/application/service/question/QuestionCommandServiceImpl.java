package com.coredisc.application.service.question;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.QuestionConverter;
import com.coredisc.common.exception.handler.QuestionHandler;
import com.coredisc.domain.category.Category;
import com.coredisc.domain.category.CategoryRepository;
import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import com.coredisc.domain.mapping.questionCategory.QuestionCategoryRepository;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.personalQuestion.PersonalQuestionRepository;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final PersonalQuestionRepository personalQuestionRepository;
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

                QuestionCategory questionCategory = QuestionCategory.builder()
                        .category(category)
                        .personalQuestion(newPersonalQuestion)
                        .build();

                questionCategoryRepository.save(questionCategory);
            }
        }

        return newPersonalQuestion;
    }
}
