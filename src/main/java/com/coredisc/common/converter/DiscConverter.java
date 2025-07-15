package com.coredisc.common.converter;

import com.coredisc.domain.disc.Disc;
import com.coredisc.presentation.dto.disc.DiscResponseDTO;

import java.util.List;

public class DiscConverter {

    private DiscConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static DiscResponseDTO.DiscListDTO toDiscListDTO(List<Disc> discList) {

        List<DiscResponseDTO.DiscDTO> discDTOList = discList.stream()
                .map(DiscConverter::toDiscDTO).toList();

        return DiscResponseDTO.DiscListDTO.builder()
                .discs(discDTOList)
                .totalDiscCount(discList.size())
                .build();
    }

    public static DiscResponseDTO.DiscDTO toDiscDTO(Disc disc) {
        return DiscResponseDTO.DiscDTO.builder()
                .id(disc.getId())
                .year(disc.getYear())
                .month(disc.getMonth())
                .coverColor(disc.getCoverColor())
                .hasCoverImage(disc.hasCoverImage())
                .coverImageUrl(disc.getCoverImgUrl())
                .build();
    }
}