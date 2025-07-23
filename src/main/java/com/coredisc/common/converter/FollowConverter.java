package com.coredisc.common.converter;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FollowConverter {

    public static Follow toFollow(Member follower, Member following){

        return Follow.builder()
                .follower(follower)
                .following(following)
                .isCircle(false)
                .build();
    }

    // 팔로워, 친한 친구
    public static FollowResponseDTO.FollowerDTO toFollowerDTO(Follow follow) {
        return FollowResponseDTO.FollowerDTO.builder()
                .followerId(follow.getFollower().getId())
                .nickname(follow.getFollower().getNickname())
                .username(follow.getFollower().getUsername())
                //.profileImgDTO(ProfileImgConverter.toProfileImgDTO(follow.getFollower().getProfileImg()))
                .isCircle(follow.isCircle())
                .build();
    }

    // TODO: 하단의 toFollowerListViewDTO 삭제 후 해당 메서드로 팔로워 목록 조회 시 사용 예정
    public static FollowResponseDTO.FollowerListDTO toFollowerListDTO(int totalCount, CursorDTO cursorDTO) {
        return FollowResponseDTO.FollowerListDTO.builder()
                .totalCount(totalCount)
                .followerCursor(cursorDTO)
                .build();
    }

    // TODO: 팔로워 목록 조회 - 수정 예정
    public static FollowResponseDTO.FollowerListViewDTO toFollowerListViewDTO(List<Follow> followers) {
        List<FollowResponseDTO.FollowerDTO> dtos = followers.stream()
                .map(FollowConverter::toFollowerDTO)
                .collect(Collectors.toList());

        return FollowResponseDTO.FollowerListViewDTO.builder()
                .totalFollowerCount(followers.size())
                .followers(dtos)
                .build();
    }

    public static FollowResponseDTO.FollowResultDTO toFollowResultDTO(Follow follow) {
        return FollowResponseDTO.FollowResultDTO.builder()
                .id(follow.getId())
                .followerId(follow.getFollower().getId())
                .followingId(follow.getFollowing().getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static FollowResponseDTO.FollowingDTO toFollowingDTO(Follow follow) {
        return FollowResponseDTO.FollowingDTO.builder()
                .followingId(follow.getFollowing().getId())
                .followingNickname(follow.getFollowing().getNickname())
                .followingUsername(follow.getFollowing().getUsername())
                .build();
    }
    // TODO: 팔로잉 목록 조회 - 수정 예정
    public static FollowResponseDTO.FollowingListViewDTO toFollowingListViewDTO(List<Follow> followings) {
        List<FollowResponseDTO.FollowingDTO> dtos = followings.stream()
                .map(FollowConverter::toFollowingDTO)
                .collect(Collectors.toList());

        return FollowResponseDTO.FollowingListViewDTO.builder()
                .totalFollowingCount(followings.size())
                .followings(dtos)
                .build();
    }
}
