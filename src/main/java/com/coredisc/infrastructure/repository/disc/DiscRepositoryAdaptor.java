package com.coredisc.infrastructure.repository.disc;

import com.coredisc.domain.disc.Disc;
import com.coredisc.domain.disc.DiscRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DiscRepositoryAdaptor implements DiscRepository {

    private final JpaDiscRepository jpaDiscRepository;

    @Override
    public Disc save(Disc disc) {
        return jpaDiscRepository.save(disc);
    }

    @Override
    public Long countByMember(Member member) {

        return jpaDiscRepository.countByMember(member);
    }

    @Override
    public Page<Disc> findByMember(Member member, Pageable pageable) {
        return jpaDiscRepository.findByMember(member, pageable);
    }

    @Override
    public List<Disc> findAllByMemberOrderByYearDescMonthDesc(Member member) {
        return jpaDiscRepository.findAllByMemberOrderByYearDescMonthDesc(member);
    }

    @Override
    public Optional<Disc> findByIdAndMember(Long id, Member member) {
        return jpaDiscRepository.findByIdAndMember(id, member);
    }

    @Override
    public boolean existsByMemberAndYearAndMonth(Member member, int year, int month) {
        return jpaDiscRepository.existsByMemberAndYearAndMonth(member, year, month);
    }
}
