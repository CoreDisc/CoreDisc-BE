package com.coredisc.application.service.follow;

import com.coredisc.common.converter.FollowConverter;
import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.infrastructure.repository.follow.queryDSL.QueryFollowRepository;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowQueryServiceImpl implements FollowQueryService {

    private final FollowRepository followRepository;
    private final QueryFollowRepository queryFollowRepository;

    @Override
    public List<Follow> getFollowers(Member member) {

        return followRepository.findAllByFollowing(member);
    }

    @Override
    public List<Follow> getFollowings(Member member) {

        return followRepository.findAllByFollower(member);
    }

    @Override
    public FollowResponseDTO.FollowerListDTO getCircleFollowers(Member member, Long cursorId, Pageable pageable) {

        List<Follow> result = queryFollowRepository.findCircleFollowers(member, cursorId, pageable);

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) result.remove(pageable.getPageSize());

        List<FollowResponseDTO.FollowerDTO> dtos = result.stream()
                .map(FollowConverter::toFollowerDTO)
                .collect(Collectors.toList());

        CursorDTO<FollowResponseDTO.FollowerDTO> cursorDTO = new CursorDTO<>(dtos, hasNext);
        int totalCount = queryFollowRepository.countCircleFollowers(member);

        return FollowResponseDTO.FollowerListDTO.builder()
                .totalCount(totalCount)
                .followerCursor(cursorDTO)
                .build();
    }
}
