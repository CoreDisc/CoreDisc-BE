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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowController implements FollowControllerDocs {

    private final FollowCommandService followCommandService;
    private final FollowQueryService followQueryService;

    @PostMapping("/api/follow/{targetId}")
    public ApiResponse<FollowResponseDTO.FollowResultDTO> follow(
            @CurrentMember Member member,
            @PathVariable Long targetId
    ) {

        return ApiResponse.onSuccess(FollowConverter.toFollowResultDTO(
                followCommandService.follow(member, targetId)
        ));
    }

    @DeleteMapping("/api/followings/{targetId}")
    public ApiResponse<?> unfollow(
            @CurrentMember Member member,
            @PathVariable Long targetId
    ) {

        followCommandService.unfollow(member, targetId);
        return ApiResponse.onSuccess("성공적으로 언팔로우되었습니다.");
    }

    @GetMapping("/api/followers")
    public ApiResponse<FollowResponseDTO.FollowerListDTO> getFollowers(
            @CurrentMember Member member,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(0, size);
        return ApiResponse.onSuccess(followQueryService.getFollowers(member, cursorId, pageable));
    }

    @GetMapping("/api/followings")
    public ApiResponse<FollowResponseDTO.FollowingListDTO> getFollowings(
            @CurrentMember Member member,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(0, size);
        return ApiResponse.onSuccess(followQueryService.getFollowings(member, cursorId, pageable));
    }
}
