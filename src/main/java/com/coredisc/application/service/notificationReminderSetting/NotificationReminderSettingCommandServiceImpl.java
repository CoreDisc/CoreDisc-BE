package com.coredisc.application.service.notificationReminderSetting;

import com.coredisc.common.converter.NotificationReminderSettingConverter;
import com.coredisc.domain.mapping.notificationReminderSetting.NotificationReminderSettingRepository;
import com.coredisc.domain.member.Member;
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
}
