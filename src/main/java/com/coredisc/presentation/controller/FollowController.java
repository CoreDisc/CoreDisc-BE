package com.coredisc.presentation.controller;

import com.coredisc.application.service.follow.FollowCommandService;
import com.coredisc.application.service.follow.FollowQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.FollowConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.FollowControllerDocs;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowController implements FollowControllerDocs {

    private final FollowCommandService followCommandService;
    private final FollowQueryService followQueryService;

    @PostMapping("/api/follow/{targetId}")
    public ApiResponse<FollowResponseDTO.FollowResultDTO> follow(
            @CurrentMember Member member,
            @PathVariable long targetId
    ) {

        return ApiResponse.onSuccess(FollowConverter.toFollowResultDTO(
                followCommandService.follow(member, targetId)
        ));
    }

    @DeleteMapping("/api/followings/{targetId}")
    public ApiResponse<?> unfollow(
            @CurrentMember Member member,
            @PathVariable long targetId
    ) {

        followCommandService.unfollow(member, targetId);
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/api/followers")
    public ApiResponse<FollowResponseDTO.FollowerListViewDTO> getFollowers(
            @CurrentMember Member member
    ) {

        return ApiResponse.onSuccess(followQueryService.getFollowers(member));
    }

    @GetMapping("/api/followings")
    public ApiResponse<FollowResponseDTO.FollowingListViewDTO> getFollowings(
            @CurrentMember Member member
    ) {

        return ApiResponse.onSuccess(followQueryService.getFollowings(member));
    }
}
