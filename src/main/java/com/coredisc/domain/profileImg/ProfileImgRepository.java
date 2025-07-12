package com.coredisc.domain.profileImg;

import com.coredisc.domain.member.Member;

public interface ProfileImgRepository {

    ProfileImg findByMember(Member member);
}
