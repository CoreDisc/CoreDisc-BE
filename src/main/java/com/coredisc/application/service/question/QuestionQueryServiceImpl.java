package com.coredisc.application.service.question;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.QuestionConverter;
import com.coredisc.common.exception.handler.QuestionHandler;
import com.coredisc.domain.category.Category;
import com.coredisc.domain.category.CategoryRepository;
import com.coredisc.domain.mapping.questionCategory.QuestionCategoryRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.officialQuestion.OfficialQuestionRepository;
import com.coredisc.domain.personalQuestion.PersonalQuestionRepository;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final PersonalQuestionRepository personalQuestionRepository;
    private final OfficialQuestionRepository officialQuestionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final CategoryRepository categoryRepository;

    // 기본 질문 리스트 조회 (카테고리별)
    @Override
    public Page<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionList(Member member, Long categoryId, Pageable pageable){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.CATEGORY_NOT_FOUND));

        return personalQuestionRepository.findBasicQuestionListByCategories(member, category, pageable);
    }

    // 기본 질문 검색 리스트 조회
    @Override
    public Page<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionSearchList(Member member, String keyword, Pageable pageable){

        return personalQuestionRepository.findBasicQuestionListByKeyword(member, keyword, pageable);
    }

    // 내가 발행한 공유 질문 리스트 조회
    @Override
    public QuestionResponseDTO.MySharedQuestionListResultDTO getMySharedQuestionList(Member member, Long categoryId, Pageable pageable){

        Long mySharedQuestionTotal = officialQuestionRepository.countOfficialQuestionByMember(member);

        Page<OfficialQuestion> mySharedQuestionsList;

        if (categoryId == null || categoryId == 0) {    // 전체 조회
            mySharedQuestionsList = officialQuestionRepository.findAllByMemberOrderByCreatedAtDesc(member, pageable);
        } else {    // 카테고리별 조회
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.CATEGORY_NOT_FOUND));
            mySharedQuestionsList = officialQuestionRepository.findAllByMemberAndCategory(member, category, pageable);
        }

        Page<QuestionResponseDTO.MySharedQuestionResultDTO> mySharedQuestionPage =
                QuestionConverter.toMySharedQuestionResultDTOPage(mySharedQuestionsList, pageable);

        return QuestionConverter.toMySharedQuestionListResultDTO(mySharedQuestionPage, mySharedQuestionTotal);
    }
}
