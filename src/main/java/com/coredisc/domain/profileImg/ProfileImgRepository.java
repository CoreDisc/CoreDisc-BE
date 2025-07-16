package com.coredisc.domain.profileImg;

import com.coredisc.domain.member.Member;

import java.util.Optional;

public interface ProfileImgRepository {

    ProfileImg findByMember(Member member);
    Optional<ProfileImg> findById(Long profileImgId);
}
