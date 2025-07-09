package com.coredisc.presentation.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class AuthRequestDTO {

    @Getter
    public static class SignupDTO {

        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        @Schema(description = "email", example = "coredisc1234@gmail.com")
        String email;

        @NotBlank(message = "이름 입력은 필수입니다.")
        @Size(max = 16, message = "이름은 16자 이내로 입력해주세요.")
        @Schema(description = "name", example = "한소희")
        String name;

        @NotBlank(message = "닉네임 입력은 필수입니다.")
        @Size(max = 16, message = "닉네임은 16자 이내로 입력해주세요.")
        @Schema(description = "nickname", example = "코어디스크")
        String nickname;

        @NotBlank(message = "계정명 입력은 필수입니다.")
        @Size(max = 16, message = "계정명은 16자 이내로 입력해주세요.")
        @Schema(description = "username", example = "my_coredisc")
        String username;

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        @Pattern(
                regexp = "^(?!.*(\\d)\\1{2})(?=(.*[A-Za-z]){1})(?=(.*\\d){1})(?!.*\\s).{10,}$|^(?!.*(\\d)\\1{2})(?=(.*[A-Za-z]){1})(?=(.*[^A-Za-z0-9]){1})(?!.*\\s).{10,}$|^(?!.*(\\d)\\1{2})(?=(.*\\d){1})(?=(.*[^A-Za-z0-9]){1})(?!.*\\s).{10,}$",
                message = "비밀번호는 영문, 숫자, 특수문자 중 2종류 이상을 조합하여 10자리 이상이어야 하며, 동일한 숫자 3개 이상을 연속해서 사용할 수 없습니다."
        )
        @Schema(description = "password", example = "coredisc1234!")
        String password;

        @NotBlank(message = "비밀번호 재확인 입력은 필수입니다.")
        @Schema(description = "passwordCheck", example = "coredisc1234!")
        String passwordCheck;
    }

    @Getter
    public static class VerifyEmailDTO {

        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Email(message = "이메일 형식이 맞지 않습니다.")
        String email;
    }

    @Getter
    public static class VerifyCodeDTO {

        @NotBlank(message = "인증 코드 입력은 필수입니다.")
        String code;

        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Email(message = "이메일 형식이 맞지 않습니다.")
        String email;
    }
}
