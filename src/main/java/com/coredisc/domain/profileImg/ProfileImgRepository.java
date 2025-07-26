package com.coredisc.domain.profileImg;

import com.coredisc.domain.member.Member;

import java.util.Optional;

public interface ProfileImgRepository {

    ProfileImg findByMember(Member member);
    Optional<ProfileImg> findById(Long profileImgId);
    void delete(ProfileImg profileImg);
    void save(ProfileImg newProfileImg);
}
