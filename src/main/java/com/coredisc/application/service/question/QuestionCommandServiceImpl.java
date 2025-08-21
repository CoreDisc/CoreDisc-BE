package com.coredisc.application.service.question;

import com.coredisc.application.service.fcm.FcmService;
import com.coredisc.application.service.notification.NotificationCommandService;
import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.QuestionCategoryConverter;
import com.coredisc.common.converter.QuestionConverter;
import com.coredisc.common.exception.handler.QuestionHandler;
import com.coredisc.domain.common.enums.NotificationType;
import com.coredisc.domain.common.enums.QuestionScope;
import com.coredisc.domain.common.enums.QuestionType;
import com.coredisc.domain.device.Device;
import com.coredisc.domain.device.DeviceRepository;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestion;
import com.coredisc.domain.mapping.memberOfficialQuestion.MemberOfficialQuestionRepository;
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
import com.coredisc.presentation.dto.notification.NotificationRequestDTO;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final PersonalQuestionRepository personalQuestionRepository;
    private final OfficialQuestionRepository officialQuestionRepository;
    private final TodayQuestionRepository todayQuestionRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final MemberOfficialQuestionRepository memberOfficialQuestionRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationCommandService notificationCommandService;
    private final FcmService fcmService;

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
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
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

        // member_official_question에 추가 여부
        if (memberOfficialQuestionRepository.existsByMemberIdAndPersonalQuestionId(member.getId(), questionId)) {

        }
        
        // 선택된 카테고리 삭제
        questionCategoryRepository.deleteByPersonalQuestion(existPersonalQuestion);

        personalQuestionRepository.deleteById(questionId);
    }


    // 타사용자가 작성한 공유 질문 저장
    @Override
    @Transactional
    public MemberOfficialQuestion saveMemberOfficialQuestion(Member member, Long questionId, QuestionRequestDTO.SaveMemberOfficialQuestionDTO request) {

        OfficialQuestion selectedOfficialQuestion = null;

        MemberOfficialQuestion memberOfficialQuestion = null;

        // 기본 or 공유 질문
        if ((request.getSelectedQuestionType() == QuestionScope.OFFICIAL) || (request.getSelectedQuestionType() == QuestionScope.DEFAULT)) {

            selectedOfficialQuestion = officialQuestionRepository.findById(questionId)
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.OFFICIAL_QUESTION_NOT_FOUND));

            // 이미 저장 여부
            if (memberOfficialQuestionRepository.findByMemberAndOfficialQuestion(member, selectedOfficialQuestion).isPresent())
                throw new QuestionHandler(ErrorStatus.ALREADY_SAVED_OFFICIAL_QUESTION);

            memberOfficialQuestion = memberOfficialQuestionRepository.save(QuestionConverter.toMemberOfficialQuestion(member, selectedOfficialQuestion, request));
        } else if (request.getSelectedQuestionType() == QuestionScope.PERSONAL) {   // 개인 질문

            PersonalQuestion selectedPersonalQuestion = personalQuestionRepository.findById(questionId)
                    .orElseThrow(() -> new QuestionHandler(ErrorStatus.PERSONAL_QUESTION_NOT_FOUND));

            // 이미 저장 여부
            if (memberOfficialQuestionRepository.findByMemberAndPersonalQuestion(member, selectedPersonalQuestion).isPresent())
                throw new QuestionHandler(ErrorStatus.ALREADY_SAVED_PERSONAL_QUESTION);

            memberOfficialQuestion = memberOfficialQuestionRepository.save(QuestionConverter.toMemberPersonalQuestion(member, selectedPersonalQuestion, request));
        } else {
            throw new QuestionHandler(ErrorStatus.QUESTION_TYPE_NOT_FOUND);
        }

        
        // 알림 관련
        if ((request.getSelectedQuestionType() == QuestionScope.OFFICIAL)       // 기본 질문 여부 & 작성자 본인 여부 확인
                && selectedOfficialQuestion != null
                && selectedOfficialQuestion.isShared()
                && !(Objects.equals(selectedOfficialQuestion.getMember(), member))) {  

            notificationCommandService.createNotification(
                    new NotificationRequestDTO(
                            NotificationType.SHARED_SAVED, // 알림 타입(공유질문 저장)
                            member, // 알림 sender
                            selectedOfficialQuestion.getMember(), // 알림 receiver (공유 질문 작성자)
                            member.getNickname() + "님이 질문을 저장했어요.",
                            selectedOfficialQuestion.getId() // 공유질문 id 전달
                    )
            );

            List<Device> devices = deviceRepository.findByMemberAndIsActiveTrue(selectedOfficialQuestion.getMember());

            // 푸시 알림 내용 설정
            String title = "CoreDisc";
            String body = member.getNickname()+"님이 질문을 저장했어요.";

            // 푸시 알림에 보낼 데이터 설정 (알림 타입 및 알림 클릭 -> 이동할 targetId)
            Map<String, String> data = new HashMap<>();
            data.put("notificationType", NotificationType.SHARED_SAVED.name());
            data.put("targetId", String.valueOf(selectedOfficialQuestion.getId()));

            for (Device device : devices) {
                String token = device.getToken();
                if (fcmService.isTokenValid(token)) {
                    fcmService.sendNotificationToToken(token, title, body, data);
                    log.info("공유질문 저장 알림 발송됨: memberId={}, token={}", selectedOfficialQuestion.getMember().getId(), token);
                } else {
                    log.warn("공유질문 저장 알림 발송 안됨: 유효하지 않은 토큰 발견. memberId={}, token={}", selectedOfficialQuestion.getMember().getId(), token);
                }
            }

            // 공유질문 저장 100회 단위 저장 알림
            Long saveCount = memberOfficialQuestionRepository.countByOfficialQuestion(selectedOfficialQuestion);
            if (saveCount % 100 == 0) {
                notificationCommandService.createNotification(
                        new NotificationRequestDTO(
                                NotificationType.SHARED_SAVED,
                                member,
                                selectedOfficialQuestion.getMember(),
                                saveCount + "명의 사용자가 질문을 저장했어요.",
                                selectedOfficialQuestion.getId()
                        )
                );

                // 공유질문 100회 단위 저장 푸시 알림 내용 설정
                title = "CoreDisc";
                body = saveCount+"명의 사용자가 질문을 저장했어요.";

                for (Device device : devices) {
                    String token = device.getToken();
                    if (fcmService.isTokenValid(token)) {
                        fcmService.sendNotificationToToken(token, title, body, data);
                        log.info("공유질문 100회 단위 저장 알림 발송됨: memberId={}, token={}", selectedOfficialQuestion.getMember().getId(), token);
                    } else {
                        log.warn("공유질문 100회 단위 저장 알림 발송 안됨: 유효하지 않은 토큰 발견. memberId={}, token={}", selectedOfficialQuestion.getMember().getId(), token);
                    }
                }
            }

        }

        return memberOfficialQuestion;
    }

    // 저장헀던 공유 질문을 삭제
    @Override
    @Transactional
    public void deleteMemberOfficialQuestion(Member member, Long savedId) {

        MemberOfficialQuestion memberOfficialQuestion = memberOfficialQuestionRepository.findByMemberAndId(member, savedId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.MEMBER_OFFICIAL_QUESTION_NOT_FOUND));

        memberOfficialQuestionRepository.delete(memberOfficialQuestion);
    }

}
