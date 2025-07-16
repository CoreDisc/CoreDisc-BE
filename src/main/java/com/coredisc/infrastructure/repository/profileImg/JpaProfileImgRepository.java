package com.coredisc.infrastructure.repository.profileImg;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.profileImg.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProfileImgRepository extends JpaRepository<ProfileImg, Long> {

    ProfileImg findByMember(Member member);
}
