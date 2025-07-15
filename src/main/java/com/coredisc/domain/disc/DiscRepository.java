package com.coredisc.domain.disc;

import com.coredisc.domain.member.Member;

public interface DiscRepository {

    Long countByMember(Member member);
}
