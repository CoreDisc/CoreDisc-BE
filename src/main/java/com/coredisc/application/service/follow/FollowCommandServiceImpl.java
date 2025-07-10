package com.coredisc.application.service.follow;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.FollowConverter;
import com.coredisc.common.exception.handler.FollowHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowCommandServiceImpl implements FollowCommandService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    //TODO: 반환 FollowResultDTO로 수정하기
    @Override
    public Follow follow(Long followerId, Long followingId) {

        if (followerId.equals(followingId)) {
            throw new FollowHandler(ErrorStatus.SELF_FOLLOW_NOT_ALLOWED);
        }

        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member following = memberRepository.findById(followerId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 이미 팔로우한 이력이 있을 경우
        if (followRepository.existsByFollowerAndFollowing(follower, following)){
            throw new FollowHandler(ErrorStatus.ALREADY_FOLLOWING);
        }

        Follow follow = FollowConverter.toFollow(follower, following);

        return followRepository.save(follow);
    }

    @Override
    public void unfollow(Long followerId, Long followingId) {

        if (followerId.equals(followingId)) {
            throw new FollowHandler(ErrorStatus.SELF_UNFOLLOW_NOT_ALLOWED);
        }

        Member follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member following = memberRepository.findById(followerId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 팔로우한 이력이 없을 경우
        if (!followRepository.existsByFollowerAndFollowing(follower, following)){
            throw new FollowHandler(ErrorStatus.FOLLOW_NOT_FOUND);
        }

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following);

        followRepository.delete(follow);
    }

}
