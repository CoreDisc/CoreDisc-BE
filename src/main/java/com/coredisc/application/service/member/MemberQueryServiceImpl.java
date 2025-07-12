package com.coredisc.application.service.member;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.exception.handler.MemberHandler;
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
    public MemberResponseDTO.MyHomeInfoOfMeDTO getMyHomeInfoOfMe(Member member) {

        // 사용자의 팔로워 수
        Long followerCount = followRepository.countByFollowingId(member.getId());

        // 사용자의 팔로잉 수
        Long followingCount = followRepository.countByFollowerId(member.getId());

        // TODO: 총 디스크(월별 리포트 수)

        // 사용자의 프로필 이미지
        ProfileImg profileImg = profileImgRepository.findByMember(member);

        return MemberConverter.toMyHomeInfoOfMeDTO(member, followerCount, followingCount, profileImg);
    }

    @Override
    public MemberResponseDTO.MyHomeInfoOfOtherDTO getMyHomeInfoOfOther(Member member, String targetUsername) {

        // 타사용자
        Member targetMember = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 타사용자의 팔로워 수
        Long followerCount = followRepository.countByFollowingId(targetMember.getId());

        // 타사용자의 팔로잉 수
        Long followingCount = followRepository.countByFollowerId(targetMember.getId());

        // TODO: 총 디스크(월별 리포트 수)

        // 타사용자의 프로필 이미지
        ProfileImg profileImg = profileImgRepository.findByMember(targetMember);

        // 팔로우 여부
        Boolean isFollowing = followRepository.existsByFollowerAndFollowing(member, targetMember);

        return MemberConverter.toMyHomeInfoOfOtherDTO(targetMember, followerCount, followingCount, profileImg, isFollowing);
    }
}
