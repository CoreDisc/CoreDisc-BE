package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.follow.Follow;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Follow", description = "팔로우 관련 API")
public interface FollowControllerDocs {

    @Operation(summary = "팔로우", description = "팔로우 기능입니다.")
    ApiResponse<Follow> follow(@PathVariable long followingId);

    @Operation(summary = "팔로우 취소", description = "팔로우 취소(팔로윙 삭제) 기능입니다.")
    ApiResponse<?> unfollow(@PathVariable long followingId);

    @Operation(summary = "팔로워 목록 조회", description = "팔로워 목록 조회 기능입니다.")
    ApiResponse<FollowResponseDTO.FollowerListViewDTO> getFollowers();
}
