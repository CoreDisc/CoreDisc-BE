package com.coredisc.infrastructure.repository.follow;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryAdaptor implements FollowRepository {

    private final JpaFollowRepository jpaFollowRepository;

    @Override
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

}
