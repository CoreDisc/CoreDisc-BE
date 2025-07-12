package com.coredisc.infrastructure.repository.profileImg;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.domain.profileImg.ProfileImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProfileImgRepositoryAdaptor implements ProfileImgRepository {

    private final JpaProfileImgRepository jpaProfileImgRepository;

    @Override
    public ProfileImg findByMember(Member member) {
        return jpaProfileImgRepository.findByMember(member);
    }
}
