package com.coredisc.presentation.controller;

import com.coredisc.application.service.follow.FollowCommandService;
import com.coredisc.application.service.follow.FollowQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.FollowConverter;
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
    @PostMapping("/api/follow/{targetId}")
    public ApiResponse<FollowResponseDTO.FollowResultDTO> follow(@PathVariable long targetId) {
        // 하드코딩
        Long memberId = 1L;
        return ApiResponse.onSuccess(FollowConverter.toFollowResultDTO(
                followCommandService.follow(memberId, targetId)
        ));
    }

    @DeleteMapping("/api/followings/{targetId}")
    public ApiResponse<?> unfollow(@PathVariable long targetId) {
        // 하드코딩
        Long memberId = 1L;
        followCommandService.unfollow(memberId, targetId);
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/api/followers")
    public ApiResponse<FollowResponseDTO.FollowerListViewDTO> getFollowers() {
        // 하드코딩
        Long memberId = 1L;
        return ApiResponse.onSuccess(followQueryService.getFollowers(memberId));
    }
}
