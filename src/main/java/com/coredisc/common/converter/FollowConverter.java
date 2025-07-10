package com.coredisc.common.converter;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FollowConverter {

    //TODO : isCircle -> false 디폴트로 수정
    public static Follow toFollow(Member follower, Member following){

        return Follow.builder()
                .follower(follower)
                .following(following)
                .isCircle(false)
                .build();
    }

    //TODO: profileImageUrl 추후 추가
    public static FollowResponseDTO.FollowerDTO toFollowerDTO(Follow follow) {
        return FollowResponseDTO.FollowerDTO.builder()
                .followerId(follow.getFollower().getId())
                .followerNickname(follow.getFollower().getNickname())
                .followerUsername(follow.getFollower().getUsername())
                .build();
    }

    public static FollowResponseDTO.FollowerListViewDTO toFollowerListViewDTO(List<Follow> follows) {
        List<FollowResponseDTO.FollowerDTO> dtos = follows.stream()
                .map(FollowConverter::toFollowerDTO)
                .collect(Collectors.toList());

        return FollowResponseDTO.FollowerListViewDTO.builder()
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

    public static FollowResponseDTO.FollowingListViewDTO toFollowingListViewDTO(List<Follow> followings) {
        List<FollowResponseDTO.FollowingDTO> dtos = followings.stream()
                .map(FollowConverter::toFollowingDTO)
                .collect(Collectors.toList());

        return FollowResponseDTO.FollowingListViewDTO.builder()
                .followings(dtos)
                .build();
    }
}
