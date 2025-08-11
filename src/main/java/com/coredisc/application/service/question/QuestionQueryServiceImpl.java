package com.coredisc.application.service.question;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.QuestionConverter;
import com.coredisc.common.exception.handler.QuestionHandler;
import com.coredisc.domain.category.Category;
import com.coredisc.domain.category.CategoryRepository;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestionRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import com.coredisc.domain.officialQuestion.OfficialQuestionRepository;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.todayQuestion.TodayQuestionRepository;
import com.coredisc.infrastructure.repository.question.CustomQuestionRepository;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final OfficialQuestionRepository officialQuestionRepository;
    private final CustomQuestionRepository customQuestionRepository;
    private final TodayQuestionRepository todayQuestionRepository;
    private final MemberOfficialQuestionRepository memberOfficialQuestionRepository;
    private final CategoryRepository categoryRepository;

    // 기본 질문 리스트 조회 (카테고리별)
    @Override
    public CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionList(
            Member member,
            Long categoryId,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize) {

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.CATEGORY_NOT_FOUND));

        CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> cursorDTO =
                customQuestionRepository.findBasicQuestionListByCategories(
                        member.getId(), categoryId, cursorCreatedAt, cursorQuestionType, cursorId, pageSize);

        List<TodayQuestion> myTodayQuestionList = new ArrayList<>();

        for (int order = 1; order <= 4; order++) {
            Optional<TodayQuestion> todayQuestion;

            if (order == 4) {
                todayQuestion = todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDate(member, order, today);
            } else {
                todayQuestion = todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(member, order, startOfMonth, endOfMonth);
            }

            todayQuestion.ifPresent(myTodayQuestionList::add);
        }

        List<QuestionResponseDTO.BasicQuestionResultDTO> basicQuestionList = cursorDTO.getValues().stream()
                .map(basicQuestionResultDTO -> {
                    // 오늘 질문 여부
                    boolean isSelected = myTodayQuestionList.stream()
                            .anyMatch(q ->
                                    (
                                            ("OFFICIAL".equals(basicQuestionResultDTO.getQuestionType()) || "DEFAULT".equals(basicQuestionResultDTO.getQuestionType()))
                                                    && q.getOfficialQuestion() != null
                                                    && q.getOfficialQuestion().getId().equals(basicQuestionResultDTO.getId())
                                    )
                                            ||
                                            (
                                                    "PERSONAL".equals(basicQuestionResultDTO.getQuestionType())
                                                            && q.getPersonalQuestion() != null
                                                            && q.getPersonalQuestion().getId().equals(basicQuestionResultDTO.getId())
                                            )
                            );

                    // 즐겨찾기 여부
                    boolean isFavorite = false;
                    if ("OFFICIAL".equals(basicQuestionResultDTO.getQuestionType()) || "DEFAULT".equals(basicQuestionResultDTO.getQuestionType())) {
                        Boolean officialFavorite = officialQuestionRepository.findIsFavoriteByIdAndMember(basicQuestionResultDTO.getId(), member);
                        if (Boolean.TRUE.equals(officialFavorite)) {
                            isFavorite = true;
                        } else {
                            isFavorite = memberOfficialQuestionRepository
                                    .existsByMemberIdAndOfficialQuestionIdAndIsFavoriteTrue(member.getId(), basicQuestionResultDTO.getId());
                        }
                    } else if ("PERSONAL".equals(basicQuestionResultDTO.getQuestionType())) {
                        isFavorite = false;
                    }

                    return QuestionConverter.toBasicQuestionResultDTO(basicQuestionResultDTO, isSelected, isFavorite);
                })
                .toList();

        return new CursorDTO<>(basicQuestionList, cursorDTO.getHasNext());
    }

    // 기본 질문 검색 리스트 조회
    @Override
    public CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> getBasicQuestionSearchList(
            Member member,
            Long categoryId,
            String keyword,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize){

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.CATEGORY_NOT_FOUND));

        CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> cursorDTO =
                customQuestionRepository.findBasicQuestionListByKeyword(member.getId(), categoryId, keyword, cursorCreatedAt, cursorQuestionType, cursorId, pageSize);

        List<TodayQuestion> myTodayQuestionList = new ArrayList<>();

        for (int order = 1; order <= 4; order++) {
            Optional<TodayQuestion> todayQuestion;

            if (order == 4) {
                todayQuestion = todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDate(member, order, today);
            } else {
                todayQuestion = todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(member, order, startOfMonth, endOfMonth);
            }

            todayQuestion.ifPresent(myTodayQuestionList::add);
        }

        List<QuestionResponseDTO.BasicQuestionResultDTO> basicQuestionList = cursorDTO.getValues().stream()
                .map(basicQuestionResultDTO -> {
                    // 오늘 질문 여부
                    boolean isSelected = myTodayQuestionList.stream()
                            .anyMatch(q ->
                                    (
                                            ("OFFICIAL".equals(basicQuestionResultDTO.getQuestionType()) || "DEFAULT".equals(basicQuestionResultDTO.getQuestionType()))
                                                    && q.getOfficialQuestion() != null
                                                    && q.getOfficialQuestion().getId().equals(basicQuestionResultDTO.getId())
                                    )
                                            ||
                                            (
                                                    "PERSONAL".equals(basicQuestionResultDTO.getQuestionType())
                                                            && q.getPersonalQuestion() != null
                                                            && q.getPersonalQuestion().getId().equals(basicQuestionResultDTO.getId())
                                            )
                            );

                    // 즐겨찾기 여부
                    boolean isFavorite = false;
                    if ("OFFICIAL".equals(basicQuestionResultDTO.getQuestionType()) || "DEFAULT".equals(basicQuestionResultDTO.getQuestionType())) {
                        Boolean officialFavorite = officialQuestionRepository.findIsFavoriteByIdAndMember(basicQuestionResultDTO.getId(), member);
                        if (Boolean.TRUE.equals(officialFavorite)) {
                            isFavorite = true;
                        } else {
                            isFavorite = memberOfficialQuestionRepository
                                    .existsByMemberIdAndOfficialQuestionIdAndIsFavoriteTrue(member.getId(), basicQuestionResultDTO.getId());
                        }
                    } else if ("PERSONAL".equals(basicQuestionResultDTO.getQuestionType())) {
                        isFavorite = false;
                    }

                    return QuestionConverter.toBasicQuestionResultDTO(basicQuestionResultDTO, isSelected, isFavorite);
                })
                .toList();

        return new CursorDTO<>(basicQuestionList, cursorDTO.getHasNext());
    }

    // 내가 발행한 공유질문 리스트 조회 (개수 포함 ver)
    @Override
    public QuestionResponseDTO.MySharedQuestionPreviewListResultDTO getMySharedQuestionPreviewList(Member member, Long cursorId, int pageSize) {

        Long mySharedQuestionTotal = officialQuestionRepository.countOfficialQuestionByMember(member);

        List<OfficialQuestion> mySharedQuestionsList = officialQuestionRepository.findAllByMemberAndCursor(member, cursorId, pageSize);

        boolean hasNext = mySharedQuestionsList.size() > pageSize;

        if (hasNext) {
            mySharedQuestionsList = mySharedQuestionsList.subList(0, pageSize);
        }

        List<QuestionResponseDTO.MySharedQuestionPreviewResultDTO> mySharedQuestionDTOList =
                mySharedQuestionsList.stream()
                        .map(question -> {
                            long sharedCount = memberOfficialQuestionRepository.countByOfficialQuestion(question);
                            return QuestionConverter.toMySharedQuestionPreviewResultDTO(question, sharedCount);
                        })
                        .toList();

        CursorDTO<QuestionResponseDTO.MySharedQuestionPreviewResultDTO> cursorDTO =
                new CursorDTO<>(mySharedQuestionDTOList, hasNext);

        return QuestionResponseDTO.MySharedQuestionPreviewListResultDTO.builder()
                .mySharedQuestionCnt(mySharedQuestionTotal)
                .mySharedQuestionList(cursorDTO)
                .build();
    }

    // 내가 발행한 공유 질문 리스트 조회 (카테고리 필터링 포함)
    @Override
    public CursorDTO<QuestionResponseDTO.MySharedQuestionResultDTO> getMySharedQuestionList(Member member, Long categoryId, Boolean favorite, Long cursorId, int pageSize){

        if (Boolean.TRUE.equals(favorite)  && categoryId != null) {   // 즐겨찾기랑 카테고리 동시 필터링 방지
            throw new QuestionHandler(ErrorStatus.INVALID_OFFICIAL_QUESTION_FILTER_COMBINATION);
        }

        List<OfficialQuestion> mySharedQuestionsList;

        if (Boolean.TRUE.equals(favorite)) {    // 즐겨찾기
            mySharedQuestionsList = officialQuestionRepository.findFavoritesByMember(member, cursorId, pageSize);
        }
        else if (categoryId == null || categoryId == 0) {    // 전체 조회
            mySharedQuestionsList = officialQuestionRepository.findAllByMemberAndCursor(member, cursorId, pageSize);
        } else {    // 카테고리별 조회
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.CATEGORY_NOT_FOUND));
            mySharedQuestionsList = officialQuestionRepository.findAllByMemberAndCategory(member, category, cursorId, pageSize);
        }

        boolean hasNext = mySharedQuestionsList.size() > pageSize;

        if (hasNext) {
            mySharedQuestionsList = mySharedQuestionsList.subList(0, pageSize);
        }

        List<QuestionResponseDTO.MySharedQuestionResultDTO> mySharedQuestionDTOList =
                mySharedQuestionsList.stream()
                        .map(question -> {
                            long sharedCount = memberOfficialQuestionRepository.countByOfficialQuestion(question);
                            return QuestionConverter.toMySharedQuestionResultDTO(question, sharedCount);
                        })
                        .toList();

        return new CursorDTO<>(mySharedQuestionDTOList, hasNext);
    }

    // 선택한 고정&랜덤 질문 조회
    @Override
    public List<QuestionResponseDTO.SelectedTodayQuestionResultDTO> getMyTodayQuestion(Member member) {

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        List<QuestionResponseDTO.SelectedTodayQuestionResultDTO> myTodayQuestionList = new ArrayList<>();

        for (int order = 1; order <= 4; order++) {
            Optional<TodayQuestion> todayQuestion;

            if (order == 4) {
                todayQuestion = todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDate(member, order, today);
            } else {
                todayQuestion = todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(member, order, startOfMonth, endOfMonth);
            }

            QuestionResponseDTO.SelectedTodayQuestionResultDTO selectedTodayQuestionResultDTO =
                    QuestionConverter.toSelectedTodayQuestionResultDTO(todayQuestion, order);

            myTodayQuestionList.add(selectedTodayQuestionResultDTO);
        }

        return myTodayQuestionList;
    }

    // 저장한 공유 질문 목록 조회
    @Override
    public CursorDTO<QuestionResponseDTO.SavedSharedQuestionResultDTO> getSavedSharedQuestionList(
            Member member,
            Long categoryId,
            Boolean favorite,
            Long cursorId,
            int pageSize) {

        if (Boolean.TRUE.equals(favorite)  && categoryId != null) {   // 즐겨찾기랑 카테고리 동시 필터링 방지
            throw new QuestionHandler(ErrorStatus.INVALID_OFFICIAL_QUESTION_FILTER_COMBINATION);
        }

        List<MemberOfficialQuestion> mySavedSharedQuestionList;

        if (Boolean.TRUE.equals(favorite) ) {
            mySavedSharedQuestionList = memberOfficialQuestionRepository.findFavoritesByMember(member, cursorId, pageSize);
        } else if (categoryId == null || categoryId == 0) { // 전체
            mySavedSharedQuestionList = memberOfficialQuestionRepository.findAllByMemberAndCursor(member, cursorId, pageSize);
        } else {    // 카테고리별
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.CATEGORY_NOT_FOUND));
            mySavedSharedQuestionList = memberOfficialQuestionRepository.findByMemberAndCategoryAndCursor(member, category, cursorId, pageSize);
        }

        boolean hasNext = mySavedSharedQuestionList.size() > pageSize;

        if (hasNext) {
            mySavedSharedQuestionList = mySavedSharedQuestionList.subList(0, pageSize);
        }

        List<QuestionResponseDTO.SavedSharedQuestionResultDTO> mySavedSharedQuestionDTOList = mySavedSharedQuestionList.stream()
                .map(question -> {
                    long sharedCount = memberOfficialQuestionRepository.countByOfficialQuestion(question.getOfficialQuestion());
                    return QuestionConverter.toSavedSharedQuestionResultDTO(question, sharedCount);
                }).toList();

        return new CursorDTO<>(mySavedSharedQuestionDTOList, hasNext);
    }

    // 인기 질문 목록 조회
    @Override
    public QuestionResponseDTO.PopularQuestionListResultDTO getPopularQuestionList() {

        LocalDate lastWeek = LocalDate.now().minusWeeks(1);
        LocalDate startOfWeek = lastWeek.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = lastWeek.with(DayOfWeek.SUNDAY);

        List<Tuple> popularQuestionTuple = officialQuestionRepository.findTop5PopularQuestionsThisWeek(startOfWeek, endOfWeek);

        // 예외 처리
        for (Tuple tuple : popularQuestionTuple) {
            OfficialQuestion question = tuple.get(0, OfficialQuestion.class);
            if (question == null) {
                throw new QuestionHandler(ErrorStatus.OFFICIAL_QUESTION_NOT_FOUND);
            }
        }

        return QuestionConverter.toPopularQuestionListResultDTO(popularQuestionTuple, startOfWeek, endOfWeek);
    }

}
