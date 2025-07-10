package com.coredisc.presentation.controller;

import com.coredisc.application.service.follow.FollowCommandService;
import com.coredisc.application.service.follow.FollowQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.follow.Follow;
import com.coredisc.presentation.controllerdocs.FollowControllerDocs;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowController implements FollowControllerDocs {

    private final FollowCommandService followCommandService;
    private final FollowQueryService followQueryService;

    //TODO: 하드코딩 수정
    @PostMapping("/api/follow/{followingId}")
    public ApiResponse<Follow> follow(long followingId) {
        // 하드코딩
        Long followId = 1L;
        return ApiResponse.onSuccess(followCommandService.follow(followId, followingId));
    }

    @DeleteMapping("/api/followings/{followingId}")
    public ApiResponse<?> unfollow(long followingId) {
        // 하드코딩
        Long followId = 1L;
        followCommandService.unfollow(followId, followingId);
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/api/followers")
    public ApiResponse<FollowResponseDTO.FollowerListViewDTO> getFollowers() {
        // 하드코딩
        Long memberId = 1L;
        return ApiResponse.onSuccess(followQueryService.getFollowers(memberId));
    }
}
