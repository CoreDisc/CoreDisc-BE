package com.coredisc.application.service.auth;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.converter.ProfileImgConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.exception.handler.ProfileImgHandler;
import com.coredisc.common.properties.SocialProperties;
import com.coredisc.common.util.RandomNicknameGenerator;
import com.coredisc.common.util.RandomUsernameGenerator;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.domain.profileImg.ProfileImgRepository;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import com.coredisc.presentation.dto.auth.KakaoUserInfo;
import com.coredisc.security.auth.PrincipalDetails;
import com.coredisc.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class SocialAuthServiceImpl implements SocialAuthService {

    private final MemberRepository memberRepository;
    private final ProfileImgRepository profileImgRepository;
    private final SocialProperties socialProperties;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;
    private final Gson gson;

    @Override
    public AuthResponseDTO.LoginResultDTO login(String provider, AuthRequestDTO.SocialLoginDTO request) {

        // yml 파일 social 아래 값 자바 객체로 매핑
        SocialProperties.ProviderProperties properties = getProviderProperties(provider);

        // 인가 코드를 이용하여 AccessToken 가져옴
        String accessToken = getAccessToken(
                URLDecoder.decode(request.getCode(), StandardCharsets.UTF_8),
                properties.getClientId(),
                properties.getClientSecret(),
                properties.getRedirectUri(),
                properties.getTokenUri()
        );

        // AccessToken을 사용하여 유저 정보 가져옴
        Object userInfo = getUserInfo(
                accessToken,
                properties.getUserInfoUri(),
                // provider에 맞는 HTTP method
                getMethod(provider),
                // provider에 맞는 클래스
                getUserInfoClass(provider)
        );

        // 로그인
        return socialLogin(provider, userInfo);
    }

    // provider에 맞는 Properties 반환
    private SocialProperties.ProviderProperties getProviderProperties(String provider) {
        switch (provider.toLowerCase()) {
            case "kakao":
                return socialProperties.getKakao();
            default:
                throw new AuthHandler(ErrorStatus.UNSUPPORTED_PROVIDER);
        }
    }

    // 인가 코드를 이용하여 AccessToken 가져옴
    private String getAccessToken(String code, String clientId, String clientSecret,
                                  String redirectUri, String tokenUri) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);

            if(response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());

                // Json 응답에서 access_token 추출
                System.out.println(jsonNode);
                return jsonNode.get("access_token").asText();
            }
        } catch (Exception e) {
            throw new AuthHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
        throw new AuthHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
    }

    // AccessToken을 사용하여 유저 정보 가져옴
    private <T> T getUserInfo(String accessToken, String userInfoUri,
                              HttpMethod httpMethod, Class<T> userInfoClass) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(userInfoUri, httpMethod, request, String.class);

            if(response.getStatusCode() == HttpStatus.OK) {
                // JSON 응답을 userInfoClass로 변환
                System.out.println(response.getBody());
                return gson.fromJson(response.getBody(), userInfoClass);
            }
        } catch (Exception e) {
            throw new AuthHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
        throw new AuthHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
    }

    // provider에 맞는 HTTP method를 반환
    private HttpMethod getMethod(String provider) {

        switch (provider.toLowerCase()) {
            case "kakao" :
                return HttpMethod.POST;
            default:
                throw new AuthHandler(ErrorStatus.UNSUPPORTED_PROVIDER);
        }
    }

    // provider에 맞는 클래스 반환
    private Class<?> getUserInfoClass(String provider) {

        switch (provider.toLowerCase()) {
            case "kakao":
                return KakaoUserInfo.class;
            default:
                throw new AuthHandler(ErrorStatus.UNSUPPORTED_PROVIDER);
        }
    }

    // provider에 맞는 email 반환
    private <T> String getEmail(String provider, T userInfo) {

        switch (provider.toLowerCase()) {
            case "kakao":
                return ((KakaoUserInfo) userInfo).getKakaoAccount().getEmail();
            default:
                throw new AuthHandler(ErrorStatus.UNSUPPORTED_PROVIDER);
        }
    }

    // provider에 맞는 Member 생성
    private <T> Member createMember(String provider, T userInfo, String randomNickname,
                                    String randomUsername, String password) {

        switch(provider.toLowerCase()) {
            case "kakao":
                return MemberConverter.toKakaoMember((KakaoUserInfo) userInfo, randomNickname, randomUsername, password);
            default:
                throw new AuthHandler(ErrorStatus.UNSUPPORTED_PROVIDER);
        }
    }

    // 회원가입 & 로그인
    private <T> AuthResponseDTO.LoginResultDTO socialLogin(String provider, T userInfo) {

        Optional<Member> memberOptional = memberRepository.findByEmail(getEmail(provider, userInfo));
        Member member;

        // 신규 이용자면 회원가입
        if (memberOptional.isEmpty()) {

            // 사용자 기본 프로필 이미지 설정 (기본 프로필 이미지는 DB에 pk 1로 넣어놓고 사용할 예정)
            ProfileImg defaultImg = profileImgRepository.findById(1L)
                    .orElseThrow(() -> new ProfileImgHandler(ErrorStatus.DEFAULT_PROFILE_IMG_NOT_FOUND));

            // 랜덤 Username 부여
            String randomUsername = RandomUsernameGenerator.generateRandomUsername();
            while (memberRepository.existsByUsername(randomUsername)) {
                randomUsername = RandomUsernameGenerator.generateRandomUsername();
            }

            // 랜덤 닉네임 부여
            String randomNickname = RandomNicknameGenerator.generateRandomNickname();
            while (memberRepository.existsByNickname(randomNickname)) {
                randomNickname = RandomNicknameGenerator.generateRandomNickname();
            }

            // 비밀번호 생성
            String password = passwordEncoder.encode("OAUTH_USER_" + UUID.randomUUID());

            member = createMember(provider, userInfo, randomNickname, randomUsername, password);
            memberRepository.save(member);

            ProfileImg profileImg = ProfileImgConverter.toProfileImg(member, defaultImg);
            member.setProfileImg(profileImg);
        } else {
            member = memberOptional.get();
        }

        // 로그인
        PrincipalDetails memberDetails = new PrincipalDetails(member);

        // 로그인 성공 시 토큰 생성
        String accessToken = jwtProvider.createAccessToken(memberDetails, member.getId());
        String refreshToken = jwtProvider.createRefreshToken(memberDetails, member.getId());

        return MemberConverter.toLoginResultDTO(member, accessToken, refreshToken);
    }

}
