package com.coredisc.presentation.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class MemberRequestDTO {

    @Getter
    public static class ResetPasswordDTO {

        @NotBlank(message = "아이디 입력은 필수입니다.")
        @Schema(example = "my_coredisc")
        private String username;

        @NotBlank(message = "변경할 비밀번호 입력은 필수입니다.")
        @Pattern(
                regexp = "^(?![A-Za-z]+$)(?!\\d+$)(?![^A-Za-z0-9]+$)(?!.*\\s)[A-Za-z\\d[^A-Za-z0-9]]{10,16}$",
                message = "비밀번호는 영문, 숫자, 특수문자 중 2종류 이상을 조합해 10~16자로 입력해야 하며, 공백은 사용할 수 없습니다."
        )
        @Schema(example = "coredisc123")
        private String newPassword;

        @NotBlank(message = "비밀번호 재확인은 필수입니다.")
        @Schema(example = "coredisc123")
        private String passwordCheck;
    }

    @Getter
    public static class MyHomeResetNicknameAndUsernameDTO {

        @NotBlank(message = "변경할 닉네임 입력은 필수입니다.")
        @Pattern(
                regexp = "^[a-zA-Z가-힣0-9]{1,16}$",
                message = "닉네임은 한글, 영어, 숫자만 1~16자로 입력할 수 있습니다."
        )
        private String newNickname;

        @NotBlank(message = "변경할 아이디 입력은 필수입니다.")
        @Pattern(
                regexp = "^[a-z0-9_.]{1,16}$",
                message = "아이디는 16자 이내, 영문 소문자, 숫자, 특수문자(_ 및 .)만 사용 가능합니다."
        )
        private String newUsername;
    }

    @Getter
    public static class MyHomeResetEmailDTO {

        @NotBlank(message = "변경할 이메일 입력은 필수입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        @Schema(description = "email", example = "coredisc12341@gmail.com")
        private String email;
    }

    @Getter
    public static class MyHomeResetPasswordDTO {

        @NotBlank(message = "현재 비밀번호 입력은 필수입니다.")
        @Schema(description = "password", example = "coredisc1234!")
        private String password;


        @NotBlank(message = "변경할 비밀번호 입력은 필수입니다.")
        @Pattern(
                regexp = "^(?![A-Za-z]+$)(?!\\d+$)(?![^A-Za-z0-9]+$)(?!.*\\s)[A-Za-z\\d[^A-Za-z0-9]]{10,16}$",
                message = "비밀번호는 영문, 숫자, 특수문자 중 2종류 이상을 조합해 10~16자로 입력해야 하며, 공백은 사용할 수 없습니다."
        )
        @Schema(description = "newPassword", example = "coredisc123456!")
        private String newPassword;

        @NotBlank(message = "비밀번호 재확인 입력은 필수입니다.")
        @Schema(description = "passwordCheck", example = "coredisc123456!")
        private String passwordCheck;
    }

    @Getter
    public static class MyHomeResetUsernameDTO {
        @NotBlank(message = "변경할 아이디 입력은 필수입니다.")
        @Pattern(
                regexp = "^[a-z0-9_.]{1,16}$",
                message = "아이디는 16자 이내, 영문 소문자, 숫자, 특수문자(_ 및 .)만 사용 가능합니다."
        )
        private String newUsername;
    }
}
