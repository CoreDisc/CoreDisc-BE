package com.coredisc.presentation.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class MemberRequestDTO {

    @Getter
    public static class ResetPasswordDTO {

        @NotBlank(message = "아이디 입력은 필수입니다.")
        @Schema(example = "my_coredisc")
        private String username;

        @NotBlank(message = "변경할 비밀번호 입력은 필수입니다.")
        @Pattern(
                regexp = "^(?!.*(\\d)\\1{2})(?=(.*[A-Za-z]){1})(?=(.*\\d){1})(?!.*\\s).{10,}$|^(?!.*(\\d)\\1{2})(?=(.*[A-Za-z]){1})(?=(.*[^A-Za-z0-9]){1})(?!.*\\s).{10,}$|^(?!.*(\\d)\\1{2})(?=(.*\\d){1})(?=(.*[^A-Za-z0-9]){1})(?!.*\\s).{10,}$",
                message = "비밀번호는 영문, 숫자, 특수문자 중 2종류 이상을 조합하여 10자리 이상이어야 하며, 동일한 숫자 3개 이상을 연속해서 사용할 수 없습니다."
        )
        @Schema(example = "coredisc123")
        private String newPassword;

        @NotBlank(message = "비밀번호 재확인은 필수입니다.")
        @Schema(example = "coredisc123")
        private String passwordCheck;
    }

    @Getter
    public static class ResetNicknameDTO {

        @NotBlank(message = "변경할 닉네임 입력은 필수입니다.")
        @Size(max = 16, message = "닉네임은 16자 이내로 입력해주세요.")
        private String newNickname;
    }
}
