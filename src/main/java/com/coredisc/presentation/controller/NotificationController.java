package com.coredisc.presentation.controller;

import com.coredisc.application.service.notification.NotificationCommandService;
import com.coredisc.application.service.notification.NotificationQueryService;
import com.coredisc.application.service.notificationReminderSetting.NotificationReminderSettingQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.NotificationControllerDocs;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.notification.NotificationResponseDTO;
import com.coredisc.presentation.dto.notificationReminderSetting.NotificationReminderSettingResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationControllerDocs {

    private final NotificationQueryService notificationQueryService;
    private final NotificationCommandService notificationCommandService;
    private final NotificationReminderSettingQueryService notificationReminderSettingQueryService;

    @GetMapping("/api/notifications/unread")
    public ApiResponse<Boolean> isUnreadNotifications(@CurrentMember Member member) {

        return ApiResponse.onSuccess(notificationQueryService.hasUnreadNotification(member));
    }

    @PatchMapping("/api/notifications/{notificationId}/read")
    public ApiResponse<String> readNotification(
            @CurrentMember Member member,
            @PathVariable Long notificationId
    ) {
        notificationCommandService.readNotification(member, notificationId);
        return ApiResponse.onSuccess("개별 알림 읽음 처리 성공하였습니다.");
    }

    @PatchMapping("/api/notifications/read-all")
    public ApiResponse<String> readAllNotifications(@CurrentMember Member member) {
        notificationCommandService.readAllNotifications(member);
        return ApiResponse.onSuccess("전체 알림 읽음 처리 성공하였습니다.");
    }

    @GetMapping("/api/notifications")
    public ApiResponse<CursorDTO<NotificationResponseDTO>> getNotifications(
            @CurrentMember Member member,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(0, size);
        return ApiResponse.onSuccess(notificationQueryService.getNotifications(member, cursorId, pageable));
    }

    @GetMapping("/api/notifications/reminder/setting")
    public ApiResponse<NotificationReminderSettingResponseDTO> getNotificationReminderSettings(Member member) {

        return ApiResponse.onSuccess(notificationReminderSettingQueryService.getNotificationReminderSetting(member));
    }

}
