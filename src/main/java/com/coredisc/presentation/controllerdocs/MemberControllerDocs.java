package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.dto.member.MemberRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Member", description = "멤버 관련 API")
public interface MemberControllerDocs {

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 기능입니다.")
    ApiResponse<String> resetPassword(@RequestBody @Valid MemberRequestDTO.ResetPasswordDTO request);
}
