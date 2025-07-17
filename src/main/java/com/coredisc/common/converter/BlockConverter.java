package com.coredisc.common.converter;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.block.BlockResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

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

    public static BlockResponseDTO.BlockedDTO toBlockedDTO(Block block) {
        return BlockResponseDTO.BlockedDTO.builder()
                .blockedId(block.getBlocked().getId())
                .blockedNickname(block.getBlocked().getNickname())
                .blockedUsername(block.getBlocked().getUsername())
                .build();
    }

    public static BlockResponseDTO.BlockedListViewDTO toBlockedListViewDTO(List<Block> blockeds) {
        List<BlockResponseDTO.BlockedDTO> dtos = blockeds.stream()
                .map(BlockConverter::toBlockedDTO)
                .collect(Collectors.toList());

        return BlockResponseDTO.BlockedListViewDTO.builder()
                .totalBlockedCount(blockeds.size())
                .blockedList(dtos)
                .build();
    }
}
