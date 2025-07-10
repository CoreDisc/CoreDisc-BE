package com.coredisc.application.service.follow;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.FollowConverter;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowQueryServiceImpl implements FollowQueryService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Override
    public FollowResponseDTO.FollowerListViewDTO getFollowers(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Follow> followers = followRepository.findAllByFollowing(member);

        return FollowConverter.toFollowerListViewDTO(followers);
    }
}
