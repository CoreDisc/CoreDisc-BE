package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Follow", description = "팔로우 관련 API")
public interface FollowControllerDocs {

    @Operation(summary = "팔로우", description = "팔로우 기능입니다.")
    ApiResponse<FollowResponseDTO.FollowResultDTO> follow(@CurrentMember Member member, @PathVariable long targetId);

    @Operation(summary = "팔로우 취소", description = "팔로우 취소(팔로윙 삭제) 기능입니다.")
    ApiResponse<?> unfollow(@CurrentMember Member member, @PathVariable long targetId);

    @Operation(summary = "팔로워 목록 조회", description = "팔로워 목록 조회 기능입니다.")
    ApiResponse<FollowResponseDTO.FollowerListViewDTO> getFollowers(@CurrentMember Member member);

    @Operation(summary = "팔로잉 목록 조회", description = "팔로잉 목록 조회 기능입니다.")
    ApiResponse<FollowResponseDTO.FollowingListViewDTO> getFollowings(@CurrentMember Member member);
}
