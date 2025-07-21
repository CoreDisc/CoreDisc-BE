package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Circle", description = "친한 친구 관련 API")
public interface CircleControllerDocs {

    @Operation(summary = "친한 친구 설정", description = "친한 친구 설정 기능입니다.")
    ApiResponse<String> addToCircle(@CurrentMember Member member, @PathVariable Long targetId);

    @Operation(summary = "친한 친구 삭제", description = "친한 친구 삭제 기능입니다.")
    ApiResponse<String> removeToCircle(@CurrentMember Member member, @PathVariable Long targetId);
}
