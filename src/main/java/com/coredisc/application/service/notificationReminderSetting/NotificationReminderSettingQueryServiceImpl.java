package com.coredisc.application.service.notificationReminderSetting;

import com.coredisc.common.converter.NotificationReminderSettingConverter;
import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSettingRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.notificationReminderSetting.NotificationReminderSettingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationReminderSettingQueryServiceImpl implements NotificationReminderSettingQueryService {

    private final NotificationReminderSettingRepository notificationReminderSettingRepository;

    @Override
    public NotificationReminderSettingResponseDTO getNotificationReminderSetting(Member member) {

        return notificationReminderSettingRepository.findByMember(member)
                .map(NotificationReminderSettingConverter::toResponseDTO)
                .orElseGet(NotificationReminderSettingConverter::defaultResponse);
    }
}
