package com.coredisc.application.service.notificationReminderSetting;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.NotificationReminderSettingConverter;
import com.coredisc.common.exception.handler.NotificationReminderSettingHandler;
import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSetting;
import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSettingRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.notificationReminderSetting.NotificationReminderSettingRequestDTO;
import com.coredisc.presentation.dto.notificationReminderSetting.NotificationReminderSettingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationReminderSettingCommandServiceImpl implements NotificationReminderSettingCommandService {

    private final NotificationReminderSettingRepository notificationReminderSettingRepository;

    @Override
    public void defaultNotificationReminderSetting(Member member) {
        notificationReminderSettingRepository.findByMember(member).orElseGet(() -> {
            try {
                return notificationReminderSettingRepository.save(
                        NotificationReminderSettingConverter.toDefault(member)
                );
            } catch (DataIntegrityViolationException e) {
                // 동시성 이슈- 다른 트랜잭션이 먼저 생성한 경우
                return notificationReminderSettingRepository.findByMember(member)
                        .orElseThrow();
            }
        });
    }

    @Override
    public NotificationReminderSettingResponseDTO updateNotificationReminderSetting(
            Member member,
            NotificationReminderSettingRequestDTO.NotificationReminderSettingUpdateDTO request
    ) {

        NotificationReminderSetting setting = notificationReminderSettingRepository.findByMember(member).orElseThrow();

        // 데일리 리마인더 알림 on/off 변경
        if (request.getDailyReminderEnabled() != null) {
            setting.updateDailyReminderEnabled(request.getDailyReminderEnabled());
        }

        // 데일리 리마인더 알림 시간 변경
        if (request.getDailyReminderHour() != null || request.getDailyReminderMinute() != null) {
            int m = (request.getDailyReminderMinute() != null) ? request.getDailyReminderMinute() : setting.getDailyReminderTime().getMinute();

            // 분 단위가 5의 배수가 아닌 경우 예외 발생하도록
            if (m % 5 != 0) {
                throw new NotificationReminderSettingHandler(ErrorStatus.INVALID_DAILY_REMINDER_TIME);
            }

            int h = (request.getDailyReminderHour() != null) ? request.getDailyReminderHour() : setting.getDailyReminderTime().getHour();
            setting.changeDailyReminderTime(h, m);
        }

        // unanswered 알림 on/off 변경
        if (request.getUnansweredReminderEnabled() != null) {
            setting.updateUnansweredReminderEnabled(request.getUnansweredReminderEnabled());
        }

        // unanswered 알림 시간 변경
        if (request.getUnansweredReminderHour() != null || request.getUnansweredReminderMinute() != null) {
            int m = (request.getUnansweredReminderMinute() != null) ? request.getUnansweredReminderMinute() : setting.getUnansweredReminderTime().getMinute();

            // 분 단위가 5의 배수가 아닌 경우 예외 발생
            if (m % 5 != 0) {
                throw new NotificationReminderSettingHandler(ErrorStatus.INVALID_UNANSWERED_REMINDER_TIME);
            }

            int h = (request.getUnansweredReminderHour() != null) ? request.getUnansweredReminderHour() : setting.getUnansweredReminderTime().getHour();
            setting.changeUnansweredReminderTime(h, m);
        }

        // 근데 데일리 리마인더가 off라면 unanwered도 off되도록
        if (!setting.isDailyReminderEnabled()) {
            setting.updateUnansweredReminderEnabled(false);
        }

        // 둘 다 true 일 때, unanswered 알림 시간 > 데일리 알림 시간
        if (setting.isDailyReminderEnabled() && setting.isUnansweredReminderEnabled()) {
            // Unanswered > Daily (동일 시각도 안되게)
            if (!setting.getUnansweredReminderTime().isAfter(setting.getDailyReminderTime())) {
                throw new NotificationReminderSettingHandler(ErrorStatus.INVALID_REMINDER_TIME_ORDER);
            }
        }

        return NotificationReminderSettingConverter.toResponseDTO(setting);
    }
}
