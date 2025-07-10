package com.coredisc.security.jwt.resolver;

import com.coredisc.application.service.member.MemberQueryService;
import com.coredisc.domain.Member;
import com.coredisc.security.auth.PrincipalDetails;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CurrentMemberResolver implements HandlerMethodArgumentResolver {

    private final MemberQueryService memberQueryService;

    // 리졸버가 적용될 조건
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentMember.class) && parameter.getParameterType().isAssignableFrom(Member.class);
    }

    // SecurityContext에 저장된 Authentication(인증 정보)를 가져와 로그인한 Member 반환
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            return memberQueryService.getMemberByUsername(principalDetails.getUsername());
        }
        return null;
    }
}
