package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.notification.NotificationResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Notification", description = "알림 관련 API")
public interface NotificationControllerDocs {

    // 알림 생성

    // 안읽은 알림 존재 여부 조회
    @Operation(summary = "안읽은 알림 존재 여부", description = "안읽은 알림 존재 여부 조회 기능입니다.")
    ApiResponse<Boolean> isUnreadNotifications(@CurrentMember Member member);

    // 개별 읽음 처리
    @Operation(summary = "개별 알림 읽음 처리", description = "개별 알림에 대해여 읽음 처리 기능입니다.")
    ApiResponse<String> readNotification(@CurrentMember Member member, @PathVariable Long notificationId);

    // 전체 읽음 처리
    @Operation(summary = "전체 알림 읽음 처리", description = "전체 알림에 대해여 읽음 처리 기능입니다.")
    ApiResponse<String> readAllNotifications(@CurrentMember Member member);

    // 알림 목록 조회(메인화면)
    @Operation(summary = "알림 목록 조회", description = "알림 목록 조회 기능입니다. 커서 기반 페이징입니다.")
    @Parameters({
            @Parameter(name = "cursorId", description = "마지막으로 조회한 notificationId입니다. 첫 요청 때는 null, queryString입니다."),
            @Parameter(name = "size", description = "기본값 10")
    })
    ApiResponse<CursorDTO<NotificationResponseDTO>> getNotifications(@CurrentMember Member member,
                                                                     @RequestParam(required = false) Long cursorId,
                                                                     @RequestParam(required = false) Integer size);
}
