package com.coredisc.presentation.controller;

import com.coredisc.application.service.auth.AuthCommandService;
import com.coredisc.application.service.auth.AuthQueryService;
import com.coredisc.application.service.auth.MailService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.domain.common.enums.EmailRequestType;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.AuthControllerDocs;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import com.coredisc.presentation.dto.jwt.JwtDTO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthCommandService authCommandService;
    private final AuthQueryService authQueryService;
    private final MailService mailService;

    // 회원가입
    @PostMapping("/signup")
    public ApiResponse<AuthResponseDTO.SignupResultDTO> signup(@Valid @RequestBody AuthRequestDTO.SignupDTO request) {

        return ApiResponse.onSuccess(MemberConverter.toSignupResultDTO(
                authCommandService.signup(request)
        ));
    }

    // 로그인 아이디 중복 검사
    @GetMapping("/check-username")
    public ApiResponse<AuthResponseDTO.CheckUsernameResultDTO> checkUsername(@RequestParam String username) {

        return ApiResponse.onSuccess(MemberConverter.toCheckUsernameResultDTO(
                authQueryService.checkUsername(username)
        ));
    }

    // 이메일 중복 검사
    @GetMapping("/check-email")
    public ApiResponse<AuthResponseDTO.CheckEmailResultDTO> checkEmail(@RequestParam String email) {

        return ApiResponse.onSuccess(MemberConverter.toCheckEmailResultDTO(
                authQueryService.checkEmail(email)
        ));
    }

    // 닉네임 중복 검사
    @GetMapping("/check-nickname")
    public ApiResponse<AuthResponseDTO.CheckNicknameResultDTO> checkNickname(@RequestParam String nickname) {

        return ApiResponse.onSuccess(MemberConverter.toCheckNicknameResultDTO(
                authQueryService.checkNickname(nickname)
        ));
    }

    // 인증코드 이메일 발송 (회원가입 시)
    @PostMapping("/send-code")
    public ApiResponse<String> sendCode(@RequestBody @Valid AuthRequestDTO.VerifyEmailDTO request) {

        authCommandService.sendCode(request, EmailRequestType.SIGNUP);
        return ApiResponse.onSuccess("인증 메일이 성공적으로 전송되었습니다.");
    }

    // 인증코드 검증
    @PostMapping("/verify-code")
    public ApiResponse<AuthResponseDTO.VerifyCodeResultDTO> verifyCode(@RequestBody @Valid AuthRequestDTO.VerifyCodeDTO request) {

        return ApiResponse.onSuccess(MemberConverter.toVerifyCodeResultDTO(
                authCommandService.verifyCode(request)
        ));
    }

    // 일반 로그인
    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO.LoginResultDTO> login(@RequestBody @Valid AuthRequestDTO.LoginDTO request) {

        return ApiResponse.onSuccess(
                authCommandService.login(request)
        );
    }

    // accessToken 재발급
    @PostMapping("/reissue")
    public ApiResponse<JwtDTO> reissueToken(@RequestHeader("RefreshToken") String refreshToken) {

        return ApiResponse.onSuccess(
                authCommandService.reissueToken(refreshToken)
        );
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {

        authCommandService.logout(request);
        return ApiResponse.onSuccess("로그아웃 되었습니다.");
    }

    // 아이디(username) 찾기
    @PostMapping("/username")
    public ApiResponse<AuthResponseDTO.FindUsernameResultDTO> findUsername(@RequestBody @Valid AuthRequestDTO.FindUsernameDTO request) {

        return ApiResponse.onSuccess(MemberConverter.toFindUsernameResultDTO(
                authQueryService.findUsername(request)
        ));
    }

    // 비밀번호 찾기를 위한 사용자 검증 (사용자 검증이 참이면 인증번호 메일 발송)
    @PostMapping("/password-reset/verify-user")
    public ApiResponse<String> verifyUser(@RequestBody @Valid AuthRequestDTO.VerifyUserDTO request) {

        // 사용자가 존재할 때만 이메일 전송 로직 수행
        if (authQueryService.verifyUser(request)) {
            try {
                Member member = authQueryService.findMember(request);
                mailService.sendEmail(member.getEmail(), EmailRequestType.RESET_PASSWORD);
            } catch (MessagingException | MailSendException e) {
                throw new AuthHandler(ErrorStatus.EMAIL_SEND_FAILED);
            }
        }

        // 사용자 존재 여부와 관계 없이 항상 200으로 (사용자 유추 공격 방어)
        return ApiResponse.onSuccess("이메일이 전송되었습니다.");
    }
}
