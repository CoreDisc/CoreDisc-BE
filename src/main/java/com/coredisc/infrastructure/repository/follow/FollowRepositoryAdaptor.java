package com.coredisc.infrastructure.repository.follow;

import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryAdaptor implements FollowRepository {

    private final JpaFollowRepository jpaFollowRepository;

    @Override
    public Long countByFollowerId(Long memberId) {
        return jpaFollowRepository.countByFollowerId(memberId);
    }

    @Override
    public Long countByFollowingId(Long memberId) {
        return jpaFollowRepository.countByFollowingId(memberId);
    }

    @Override
    public Boolean existsByFollowerAndFollowing(Member member, Member targetMember) {
        return jpaFollowRepository.existsByFollowerAndFollowing(member, targetMember);
    }
}
