package com.coredisc.application.service.follow;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import org.springframework.data.domain.Pageable;

public interface FollowQueryService {

    // 팔로워 목록 조회
    FollowResponseDTO.FollowerListDTO getFollowers(Member member, Long cursorId, Pageable pageable);

    // 팔로잉 목록 조회
    FollowResponseDTO.FollowingListDTO getFollowings(Member member, Long cursorId, Pageable pageable);

    // 친한 친구 목록 조회
    FollowResponseDTO.FollowerListDTO getCircleFollowers(Member member, Long cursorId, Pageable pageable);

    // 타사용자의 팔로워 목록 조회
    FollowResponseDTO.FollowerListDTO getUserFollowers(String targetUsername, Long cursorId, Pageable pageable);

    // 타사용자의 팔로잉 목록 조회
    FollowResponseDTO.FollowingListDTO getUserFollowings(String targetUsername, Long cursorId, Pageable pageable);

}
