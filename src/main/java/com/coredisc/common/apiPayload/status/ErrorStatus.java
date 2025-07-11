package com.coredisc.common.apiPayload.status;


import com.coredisc.common.apiPayload.code.BaseErrorCode;
import com.coredisc.common.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 인증 관련 에러
    PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "AUTH4001", "비밀번호가 일치하지 않습니다."),
    EMAIL_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "AUTH4002", "인증 코드가 만료되었습니다. 다시 요청해주세요."),
    CODE_NOT_EQUAL(HttpStatus.BAD_REQUEST, "AUTH4003", "인증 코드가 올바르지 않습니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUTH4004", "이미 사용 중인 계정명입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUTH4005", "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUTH4006", "이미 사용 중인 닉네임입니다."),
    DUPLICATED_RESOURCE(HttpStatus.CONFLICT, "AUTH4007", "이미 사용 중인 값이 있습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4008", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH4009", "토큰이 만료되었습니다."),
    TOKEN_LOGGED_OUT(HttpStatus.UNAUTHORIZED, "AUTH4010", "이 토큰은 로그아웃되어 더 이상 유효하지 않습니다."),

    // 인증코드 메일 전송 관련 에러
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL5001", "메일 전송에 실패했습니다."),
    EMAIL_WRITE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL5002", "메일 작성에 실패했습니다."),

    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "테스트 용도");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}