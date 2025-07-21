package com.coredisc.application.service.follow;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.FollowConverter;
import com.coredisc.common.exception.handler.CircleHandler;
import com.coredisc.common.exception.handler.FollowHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowCommandServiceImpl implements FollowCommandService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Override
    public Follow follow(Member member, Long targetId) {

        if (member.getId().equals(targetId)) {
            throw new FollowHandler(ErrorStatus.SELF_FOLLOW_NOT_ALLOWED);
        }

        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 이미 팔로우한 이력이 있을 경우
        if (followRepository.existsByFollowerAndFollowing(member, target)){
            throw new FollowHandler(ErrorStatus.ALREADY_FOLLOWING);
        }

        Follow follow = FollowConverter.toFollow(member, target);

        return followRepository.save(follow);
    }

    @Override
    public void unfollow(Member member, Long targetId) {

        if (member.getId().equals(targetId)) {
            throw new FollowHandler(ErrorStatus.SELF_UNFOLLOW_NOT_ALLOWED);
        }

        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 팔로우한 이력이 없을 경우
        if (!followRepository.existsByFollowerAndFollowing(member, target)){
            throw new FollowHandler(ErrorStatus.FOLLOW_NOT_FOUND);
        }

        Follow follow = followRepository.findByFollowerAndFollowing(member, target);

        followRepository.delete(follow);
    }

    @Override
    public void updateCircleStatus(Member member, Long targetId, boolean isCircle) {

        // 자기 자신을 친한친구로 설정하려는 경우
        if (member.getId().equals(targetId)) {
            throw new CircleHandler(ErrorStatus.SELF_CIRCLE_NOT_ALLOWED);
        }

        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 상대방이 나를 팔로우하고 있는지 확인
        // 상대방이 나를 팔로우하고 있는 경우에만 isCircle 변경 가능
        Follow follow = followRepository.findByFollowerAndFollowing(target, member);
        if (follow == null) {
            throw new FollowHandler(ErrorStatus.FOLLOWER_NOT_FOUND);
        }

        follow.updateCircle(isCircle);
    }
}
