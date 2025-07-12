package com.coredisc.application.service.member;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.domain.profileImg.ProfileImgRepository;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final ProfileImgRepository profileImgRepository;


    @Override
    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public MemberResponseDTO.MyHomeUserInfoDTO getMyHomeUserInfo(Member member) {

        // 사용자의 팔로워 수
        Long followerCount = followRepository.countByFollowingId(member.getId());

        // 사용자의 팔로잉 수
        Long followingCount = followRepository.countByFollowerId(member.getId());

        // TODO: 총 디스크(월별 리포트 수)

        // 사용자의 프로필 이미지
        ProfileImg profileImg = profileImgRepository.findByMember(member);

        return MemberConverter.toMyHomeUserInfoDTO(member, followerCount, followingCount, profileImg);
    }
}
