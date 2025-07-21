package com.coredisc.presentation.controller;

import com.coredisc.application.service.follow.FollowCommandService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.CircleControllerDocs;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CircleController implements CircleControllerDocs {

    private final FollowCommandService followCommandService;

    @PatchMapping("/api/circle/{targetId}")
    public ApiResponse<String> addToCircle(@CurrentMember Member member,
                                           @PathVariable Long targetId) {

        followCommandService.updateCircleStatus(member, targetId, true);

        return ApiResponse.onSuccess("성공적으로 친한 친구가 설정되었습니다.");
    }

    @PatchMapping("/api/circle/{targetId}")
    public ApiResponse<String> removeToCircle(@CurrentMember Member member,
                                              @PathVariable Long targetId) {

        followCommandService.updateCircleStatus(member, targetId, false);

        return ApiResponse.onSuccess("성공적으로 친한 친구가 취소되었습니다.");
    }
}
