package com.coredisc.application.service.follow;

import com.coredisc.presentation.dto.follow.FollowResponseDTO;

public interface FollowQueryService {

    // 팔로워 목록 조회
    FollowResponseDTO.FollowerListViewDTO getFollowers(Long memberId);

    // 팔로잉 목록 조회
    FollowResponseDTO.FollowingListViewDTO getFollowings(Long memberId);

}
