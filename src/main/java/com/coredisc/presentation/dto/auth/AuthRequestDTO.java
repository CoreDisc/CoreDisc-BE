package com.coredisc.presentation.dto.auth;

import com.coredisc.domain.common.enums.EmailRequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

public class AuthRequestDTO {

    @Getter
    public static class SignupDTO {

        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        @Schema(description = "email", example = "coredisc1234@gmail.com")
        private String email;

        @NotBlank(message = "이름 입력은 필수입니다.")
        @Size(max = 16, message = "이름은 16자 이내로 입력해주세요.")
        @Schema(description = "name", example = "한소희")
        private String name;

        @NotBlank(message = "닉네임 입력은 필수입니다.")
        @Size(max = 16, message = "닉네임은 16자 이내로 입력해주세요.")
        @Schema(description = "nickname", example = "코어디스크")
        private String nickname;

        @NotBlank(message = "계정명 입력은 필수입니다.")
        @Size(max = 16, message = "계정명은 16자 이내로 입력해주세요.")
        @Schema(description = "username", example = "my_coredisc")
        private String username;

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        @Pattern(
                regexp = "^(?!.*(\\d)\\1{2})(?=(.*[A-Za-z]){1})(?=(.*\\d){1})(?!.*\\s).{10,}$|^(?!.*(\\d)\\1{2})(?=(.*[A-Za-z]){1})(?=(.*[^A-Za-z0-9]){1})(?!.*\\s).{10,}$|^(?!.*(\\d)\\1{2})(?=(.*\\d){1})(?=(.*[^A-Za-z0-9]){1})(?!.*\\s).{10,}$",
                message = "비밀번호는 영문, 숫자, 특수문자 중 2종류 이상을 조합하여 10자리 이상이어야 하며, 동일한 숫자 3개 이상을 연속해서 사용할 수 없습니다."
        )
        @Schema(description = "password", example = "coredisc1234!")
        private String password;

        @NotBlank(message = "비밀번호 재확인 입력은 필수입니다.")
        @Schema(description = "passwordCheck", example = "coredisc1234!")
        private String passwordCheck;
    }

    @Getter
    public static class VerifyEmailDTO {

        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Email(message = "이메일 형식이 맞지 않습니다.")
        private String email;
    }

    @Getter
    public static class VerifyCodeDTO {

        @NotBlank(message = "아이디 입력은 필수입니다.")
        private String username;

        @NotBlank(message = "인증 코드 입력은 필수입니다.")
        private String code;

        @NotNull
        @Schema(description = "회원가입/비밀번호 변경 인증번호를 구분합니다. SIGNUP 또는 RESET_PASSWORD", example = "SIGNUP")
        private EmailRequestType emailRequestType;
    }

    @Getter
    public static class LoginDTO {

        @NotBlank(message = "아이디 입력은 필수입니다.")
        @Schema(description = "username", example = "my_coredisc")
        private String username;

        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        @Schema(description = "password", example = "coredisc1234!")
        private String password;
    }

    @Getter
    public static class FindUsernameDTO {

        @NotBlank(message = "이름 입력은 필수입니다.")
        private String name;

        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Email(message = "이메일 형식이 맞지 않습니다.")
        private String email;
    }

    @Getter
    public static class VerifyUserDTO {

        @NotBlank(message = "이름 입력은 필수입니다.")
        private String name;

        @NotBlank(message = "아이디 입력은 필수입니다.")
        private String username;
    }
}
