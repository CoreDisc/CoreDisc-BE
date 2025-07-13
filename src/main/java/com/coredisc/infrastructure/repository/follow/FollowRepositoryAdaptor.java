package com.coredisc.infrastructure.repository.follow;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public Follow save(Follow follow) {
        return jpaFollowRepository.save(follow);
    }

    @Override
    public boolean existsByFollowerAndFollowing(Member follower, Member following) {
        return jpaFollowRepository.existsByFollowerAndFollowing(follower, following);
    }

    @Override
    public Follow findByFollowerAndFollowing(Member follower, Member following) {
        return jpaFollowRepository.findByFollowerAndFollowing(follower, following);
    }

    @Override
    public void delete(Follow follow) {
        jpaFollowRepository.delete(follow);
    }

    @Override
    public List<Follow> findAllByFollowing(Member member) {
        return jpaFollowRepository.findAllByFollowing(member);
    }

    @Override
    public List<Follow> findAllByFollower(Member member) {
        return jpaFollowRepository.findAllByFollower(member);
    }

}
