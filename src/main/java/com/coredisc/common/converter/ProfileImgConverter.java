package com.coredisc.common.converter;

import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.presentation.dto.profileImg.ProfileImgResponseDTO;

public class ProfileImgConverter {

    public static ProfileImgResponseDTO.ProfileImgDTO toProfileImgDTO(ProfileImg profileImg) {

        return ProfileImgResponseDTO.ProfileImgDTO.builder()
                .profileImgId(profileImg.getId())
                .imageUrl(profileImg.getImgUrl())
                .build();
    }
}
