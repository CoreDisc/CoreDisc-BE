package com.coredisc.infrastructure.repository.disc;

import com.coredisc.domain.disc.DiscRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiscRepositoryAdaptor implements DiscRepository {

    private final JpaDiscRepository jpaDiscRepository;

    @Override
    public Long countByMember(Member member) {

        return jpaDiscRepository.countByMember(member);
    }
}
