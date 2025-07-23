package com.coredisc.application.service.follow;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowQueryService {

    // 팔로워 목록 조회
    List<Follow> getFollowers(Member member);

    // 팔로잉 목록 조회
    List<Follow> getFollowings(Member member);

    // 친한 친구 목록 조회
    FollowResponseDTO.FollowerListDTO getCircleFollowers(Member member, Long cursorId, Pageable pageable);

}
