package com.coredisc.application.service.follow;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;

public interface FollowQueryService {

    // 팔로워 목록 조회
    FollowResponseDTO.FollowerListViewDTO getFollowers(Member member);

    // 팔로잉 목록 조회
    FollowResponseDTO.FollowingListViewDTO getFollowings(Member member);

}
