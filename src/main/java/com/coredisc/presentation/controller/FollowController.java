package com.coredisc.presentation.controller;

import com.coredisc.application.service.follow.FollowCommandService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.follow.Follow;
import com.coredisc.presentation.controllerdocs.FollowControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController implements FollowControllerDocs {

    private final FollowCommandService followCommandService;

    //TODO: 하드코딩 수정
    @PostMapping("/api/follow/{followingId}")
    public ApiResponse<Follow> follow(long followingId) {
        // 하드코딩
        Long followId = 1L;
        return ApiResponse.onSuccess(followCommandService.follow(followId, followingId));
    }
}
