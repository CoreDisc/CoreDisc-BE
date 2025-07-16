package com.coredisc.application.service.auth;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.converter.MemberTermsConverter;
import com.coredisc.common.converter.ProfileImgConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.exception.handler.ProfileImgHandler;
import com.coredisc.common.exception.handler.TermsHandler;
import com.coredisc.common.util.RandomNicknameGenerator;
import com.coredisc.common.util.RedisUtil;
import com.coredisc.domain.common.enums.EmailRequestType;
import com.coredisc.domain.mapping.MemberTerms;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.domain.profileImg.ProfileImgRepository;
import com.coredisc.domain.terms.Terms;
import com.coredisc.domain.terms.TermsRepository;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import com.coredisc.presentation.dto.jwt.JwtDTO;
import com.coredisc.security.auth.PrincipalDetails;
import com.coredisc.security.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TermsRepository termsRepository;
    private final ProfileImgRepository profileImgRepository;
    private final MailService mailService;
    private final RedisUtil redisUtil;
    private final JwtProvider jwtProvider;

    // 회원가입
    @Override
    @Transactional
    public Member signup(AuthRequestDTO.SignupDTO request) {

        if(!request.getPassword().equals(request.getPasswordCheck())) {
            throw new AuthHandler(ErrorStatus.PASSWORD_NOT_EQUAL);
        }
        // 1차 방어
        if(memberRepository.existsByUsername(request.getUsername())) {
            throw new AuthHandler(ErrorStatus.USERNAME_ALREADY_EXISTS);
        }
        if(memberRepository.existsByEmail(request.getEmail())) {
            throw new AuthHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }

        // 1-1. 이용 약관 화면에서 보여준 약관 리스트
        List<Terms> validTermsList = termsRepository.findLatestTermsByType();
        // 1-2. 이용 약관 화면에서 보여준 약관 ID Set
        Set<Long> validTermsIds = getTermsIdsSet(validTermsList);

        // 2-1. 사용자가 동의한 약관 리스트
        List<Terms> agreedTermsList = request.getAgreedTermsIds().stream()
                .map(agreedTermsId ->
                        // 존재하지 않는 약관 id일 시 예외 처리
                        termsRepository.findById(agreedTermsId)
                                .orElseThrow(() -> new TermsHandler(ErrorStatus.TERMS_NOT_FOUND))
                )
                .toList();
        // 2-2. 샤용자가 동의한 약관 ID Set
        Set<Long> agreedTermsIds = getTermsIdsSet(agreedTermsList);

        // 3. 유효하지 않은 (DB에는 존재하지만 화면에 보여준 적 없는) 약관 id가 포함되어 있으면 예외 처리
        if(!validTermsIds.containsAll(agreedTermsIds)) {
            throw new AuthHandler(ErrorStatus.INVALID_TERMS_INCLUDED);
        }

        // 4-1. 회원가입 시 필수 동의 약관 리스트
        List<Terms> requiredTermsList = termsRepository.findLatestRequiredTermsGroupedByType();
        // 4-2. 회원가입 시 필수 동의 약관 ID set
        Set<Long> requiredTermsIds = getTermsIdsSet(requiredTermsList);

        // 5. 필수 동의 항목에 동의하지 않았을 시 예외 처리
        if(!agreedTermsIds.containsAll(requiredTermsIds)) {
            throw new AuthHandler(ErrorStatus.REQUIRED_TERMS_NOT_AGREED);
        }

        // 랜덤 닉네임 부여
        String nickname = RandomNicknameGenerator.generateRandomNickname();
        while (memberRepository.existsByNickname(nickname)) {
            nickname = RandomNicknameGenerator.generateRandomNickname();
        }

        Member newMember = MemberConverter.toMember(request, nickname);
        newMember.encodePassword(passwordEncoder.encode(request.getPassword()));

        // 사용자 이용약관 저장
        List<MemberTerms> memberTermsList = MemberTermsConverter.toMemberTermsList(agreedTermsList);
        memberTermsList.forEach(memberTerms -> {memberTerms.setMember(newMember);});

        // 사용자 기본 프로필 이미지 설정 (기본 프로필 이미지는 DB에 pk 1로 넣어놓고 사용할 예정)
        ProfileImg defaultImg = profileImgRepository.findById(1L)
                .orElseThrow(() -> new ProfileImgHandler(ErrorStatus.DEFAULT_PROFILE_IMG_NOT_FOUND));

        ProfileImg profileImg = ProfileImgConverter.toProfileImg(newMember, defaultImg);
        newMember.setProfileImg(profileImg);

        try {
            return memberRepository.save(newMember);
        } catch (DataIntegrityViolationException e) { // race condition 이중 방어
            throw new AuthHandler(ErrorStatus.DUPLICATED_RESOURCE);
        }
    }

    // 이메일 코드 전송
    @Override
    public void sendCode(AuthRequestDTO.VerifyEmailDTO request, EmailRequestType emailRequestType) {

        try {
            mailService.sendEmail(request.getEmail(), emailRequestType);
        } catch (MessagingException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_WRITE_FAILED);
        } catch (MailException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }

    // 코드 인증
    @Override
    public boolean verifyCode(AuthRequestDTO.VerifyCodeDTO request) {

        String authCode = (String) redisUtil.get("auth:" + request.getUsername() + ":" + request.getEmailRequestType());

        if (authCode == null) {
            throw new AuthHandler(ErrorStatus.EMAIL_CODE_EXPIRED);
        }
        if (!authCode.equals(request.getCode())) {
            throw new AuthHandler(ErrorStatus.CODE_NOT_EQUAL);
        }
        return true;
    }

    // 로그인
    @Override
    public AuthResponseDTO.LoginResultDTO login(AuthRequestDTO.LoginDTO request) {

        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AuthHandler(ErrorStatus.PASSWORD_NOT_EQUAL);
        }

        PrincipalDetails principalDetails = new PrincipalDetails(member);

        // 로그인 성공 시 토큰 생성
        String accessToken = jwtProvider.createAccessToken(principalDetails, member.getId());
        String refreshToken = jwtProvider.createRefreshToken(principalDetails, member.getId());

        return MemberConverter.toLoginResultDTO(member, accessToken, refreshToken);
    }

    // 토큰 재발급
    @Override
    public JwtDTO reissueToken(String refreshToken) {

        try {
            jwtProvider.validateRefreshToken(refreshToken);

            return jwtProvider.reissueToken(refreshToken);
        } catch (ExpiredJwtException eje) {
            throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
        } catch (IllegalArgumentException iae) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }
    }

    @Override
    public void logout(HttpServletRequest request) {

        try {
            String accessToken = jwtProvider.resolveAccessToken(request);

            // 블랙리스트에 저장
            redisUtil.set(accessToken, "logout");
            redisUtil.expire(accessToken, jwtProvider.getRemainingExpiration(accessToken), TimeUnit.MILLISECONDS);
            // RefreshToken 삭제
            redisUtil.delete(jwtProvider.getUsername(accessToken));
        } catch (ExpiredJwtException e) {
            throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
        }
    }

    private Set<Long> getTermsIdsSet(List<Terms> termsList) {

        return termsList.stream()
                .map(Terms::getId)
                .collect(Collectors.toSet());
    }
}
