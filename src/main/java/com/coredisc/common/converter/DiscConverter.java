package com.coredisc.common.converter;

import com.coredisc.domain.disc.Disc;
import com.coredisc.presentation.dto.disc.DiscResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public class DiscConverter {

    private DiscConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static DiscResponseDTO.DiscListDTO toDiscListDTO(Page<Disc> discPage) {
        List<DiscResponseDTO.DiscDTO> discDTOList = discPage.getContent().stream()
                .map(DiscConverter::toDiscDTO)
                .toList();

        return DiscResponseDTO.DiscListDTO.builder()
                .discs(discDTOList)
                .totalDiscCount((int) discPage.getTotalElements())
                .totalPages(discPage.getTotalPages())
                .currentPage(discPage.getNumber())
                .hasNext(discPage.hasNext())
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