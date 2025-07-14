package com.coredisc.common.converter;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.block.BlockResponseDTO;

public class BlockConverter {

    public static Block toBlock(Member member, Member blockedMember) {
        return Block.builder()
                .blocker(member)
                .blocked(blockedMember)
                .build();
    }

    public static BlockResponseDTO.BlockResultDTO toBlockResultDTO(Block block) {
        return  BlockResponseDTO.BlockResultDTO.builder()
                .id(block.getId())
                .blockerId(block.getBlocker().getId())
                .blockedId(block.getBlocked().getId())
                .createdAt(block.getCreatedAt())
                .build();
    }
}
