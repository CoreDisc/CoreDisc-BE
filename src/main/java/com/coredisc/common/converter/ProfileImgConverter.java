package com.coredisc.common.converter;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.presentation.dto.profileImg.ProfileImgResponseDTO;

public class ProfileImgConverter {

    private ProfileImgConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static ProfileImgResponseDTO.ProfileImgDTO toProfileImgDTO(ProfileImg profileImg) {

        return ProfileImgResponseDTO.ProfileImgDTO.builder()
                .profileImgId(profileImg.getId())
                .imageUrl(profileImg.getImgUrl())
                .build();
    }

    public static ProfileImg toProfileImg(Member member, ProfileImg defaultProfileImg) {

        return ProfileImg.builder()
                .imgUrl(defaultProfileImg.getImgUrl())
                .member(member)
                .build();
    }
}
