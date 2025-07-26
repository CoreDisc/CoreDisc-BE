package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Follow", description = "팔로우 관련 API")
public interface FollowControllerDocs {

    @Operation(summary = "팔로우", description = "팔로우 기능입니다.")
    ApiResponse<FollowResponseDTO.FollowResultDTO> follow(@CurrentMember Member member, @PathVariable Long targetId);

    @Operation(summary = "팔로우 취소", description = "팔로우 취소(팔로윙 삭제) 기능입니다.")
    ApiResponse<String> unfollow(@CurrentMember Member member, @PathVariable Long targetId);

    //TODO: description 수정
    @Operation(summary = "팔로워 목록 조회", description = "팔로워 목록 조회 기능입니다. 커서 기반 페이징입니다.")
    @Parameters({
            @Parameter(name = "cursorId", description = "마지막으로 조회한 followId입니다. 첫 요청 때는 null, queryString입니다."),
            @Parameter(name = "size", description = "기본값 10")
    })
    ApiResponse<FollowResponseDTO.FollowerListDTO> getFollowers(@CurrentMember Member member,
                                                                @RequestParam(required = false) Long cursorId,
                                                                @RequestParam(required = false) Integer size);

    @Operation(summary = "팔로잉 목록 조회", description = "팔로잉 목록 조회 기능입니다. 커서 기반 페이징입니다.")
    @Parameters({
            @Parameter(name = "cursorId", description = "마지막으로 조회한 followId입니다. 첫 요청 때는 null, queryString입니다."),
            @Parameter(name = "size", description = "기본값 10")
    })
    ApiResponse<FollowResponseDTO.FollowingListDTO> getFollowings(@CurrentMember Member member,
                                                                  @RequestParam(required = false) Long cursorId,
                                                                  @RequestParam(required = false) Integer size);
}
