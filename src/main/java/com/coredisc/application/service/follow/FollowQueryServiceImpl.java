package com.coredisc.application.service.follow;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.FollowConverter;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
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

    private final QueryFollowRepository queryFollowRepository;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Override
    public FollowResponseDTO.FollowerListDTO getFollowers(Member member, Long cursorId, Pageable pageable) {

        List<Follow> result = queryFollowRepository.findFollowers(member, cursorId, pageable);

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) result.remove(pageable.getPageSize());

        List<FollowResponseDTO.FollowerDTO> dtos = result.stream()
                .map(follow -> {
                    Member follower = follow.getFollower();
                    boolean isMutual = followRepository.existsByFollowerAndFollowing(member, follower);
                    return FollowConverter.toFollowerDTO(follow, isMutual);
                })
                .collect(Collectors.toList());

        CursorDTO<FollowResponseDTO.FollowerDTO> cursorDTO = new CursorDTO<>(dtos, hasNext);
        int totalCount = queryFollowRepository.countFollowers(member);

        return FollowConverter.toFollowerListDTO(totalCount, cursorDTO);
    }

    @Override
    public FollowResponseDTO.FollowingListDTO getFollowings(Member member, Long cursorId, Pageable pageable) {

        List<Follow> result = queryFollowRepository.findFollowings(member, cursorId, pageable);

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) result.remove(pageable.getPageSize());

        List<FollowResponseDTO.FollowingDTO> dtos = result.stream()
                .map(FollowConverter::toFollowingDTO)
                .collect(Collectors.toList());

        CursorDTO<FollowResponseDTO.FollowingDTO> cursorDTO = new CursorDTO<>(dtos, hasNext);
        int totalCount = queryFollowRepository.countFollowings(member);

        return FollowConverter.toFollowingListDTO(totalCount, cursorDTO);
    }

    @Override
    public FollowResponseDTO.FollowerListDTO getCircleFollowers(Member member, Long cursorId, Pageable pageable) {

        List<Follow> result = queryFollowRepository.findCircleFollowers(member, cursorId, pageable);

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) result.remove(pageable.getPageSize());

        List<FollowResponseDTO.FollowerDTO> dtos = result.stream()
                .map(follow -> FollowConverter.toFollowerDTO(follow, null))
                .collect(Collectors.toList());

        CursorDTO<FollowResponseDTO.FollowerDTO> cursorDTO = new CursorDTO<>(dtos, hasNext);
        int totalCount = queryFollowRepository.countCircleFollowers(member);

        return FollowConverter.toFollowerListDTO(totalCount, cursorDTO);
    }

    @Override
    public FollowResponseDTO.FollowerListDTO getUserFollowers(String targetUsername, Long cursorId, Pageable pageable) {

        Member targetMember = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Follow> result = queryFollowRepository.findFollowers(targetMember, cursorId, pageable);

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) result.remove(pageable.getPageSize());

        List<FollowResponseDTO.FollowerDTO> dtos = result.stream()
                .map(follow -> {
                    Member follower = follow.getFollower();
                    boolean isMutual = followRepository.existsByFollowerAndFollowing(targetMember, follower);
                    return FollowConverter.toFollowerDTO(follow, isMutual);
                })
                .collect(Collectors.toList());

        CursorDTO<FollowResponseDTO.FollowerDTO> cursorDTO = new CursorDTO<>(dtos, hasNext);
        int totalCount = queryFollowRepository.countFollowers(targetMember);

        return FollowConverter.toFollowerListDTO(totalCount, cursorDTO);
    }

    @Override
    public FollowResponseDTO.FollowingListDTO getUserFollowings(String targetUsername, Long cursorId, Pageable pageable) {

        Member targetMember = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Follow> result = queryFollowRepository.findFollowings(targetMember, cursorId, pageable);

        boolean hasNext = result.size() > pageable.getPageSize();
        if (hasNext) result.remove(pageable.getPageSize());

        List<FollowResponseDTO.FollowingDTO> dtos = result.stream()
                .map(FollowConverter::toFollowingDTO)
                .collect(Collectors.toList());

        CursorDTO<FollowResponseDTO.FollowingDTO> cursorDTO = new CursorDTO<>(dtos, hasNext);
        int totalCount = queryFollowRepository.countFollowings(targetMember);

        return FollowConverter.toFollowingListDTO(totalCount, cursorDTO);
    }
}
