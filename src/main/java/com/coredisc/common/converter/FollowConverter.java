package com.coredisc.common.converter;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.follow.FollowResponseDTO;

import java.time.LocalDateTime;

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

    public static FollowResponseDTO.FollowerListDTO toFollowerListDTO(int totalCount, CursorDTO cursorDTO) {
        return FollowResponseDTO.FollowerListDTO.builder()
                .totalCount(totalCount)
                .followerCursor(cursorDTO)
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
                .nickname(follow.getFollowing().getNickname())
                .username(follow.getFollowing().getUsername())
                //.profileImgDTO(ProfileImgConverter.toProfileImgDTO(follow.getFollower().getProfileImg()))
                .build();
    }

    public static FollowResponseDTO.FollowingListDTO toFollowingListDTO(int totalCount, CursorDTO cursorDTO) {
        return FollowResponseDTO.FollowingListDTO.builder()
                .totalCount(totalCount)
                .followingCursor(cursorDTO)
                .build();
    }
}
