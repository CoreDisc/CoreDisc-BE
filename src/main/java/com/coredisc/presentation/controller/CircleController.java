package com.coredisc.presentation.controller;

import com.coredisc.application.service.follow.FollowCommandService;
import com.coredisc.application.service.follow.FollowQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.CircleControllerDocs;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CircleController implements CircleControllerDocs {

    private final FollowCommandService followCommandService;
    private final FollowQueryService followQueryService;

    @PatchMapping("/api/circle/add/{targetId}")
    public ApiResponse<String> addToCircle(@CurrentMember Member member,
                                           @PathVariable Long targetId) {

        followCommandService.updateCircleStatus(member, targetId, true);

        return ApiResponse.onSuccess("성공적으로 친한 친구가 설정되었습니다.");
    }

    @PatchMapping("/api/circle/remove/{targetId}")
    public ApiResponse<String> removeToCircle(@CurrentMember Member member,
                                              @PathVariable Long targetId) {

        followCommandService.updateCircleStatus(member, targetId, false);

        return ApiResponse.onSuccess("성공적으로 친한 친구가 취소되었습니다.");
    }

    @GetMapping("/api/circles")
    public ApiResponse<FollowResponseDTO.FollowerListDTO> getCircleFollowers(
            @CurrentMember Member member,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(0, size);
        return ApiResponse.onSuccess(followQueryService.getCircleFollowers(member, cursorId, pageable));
    }
}
