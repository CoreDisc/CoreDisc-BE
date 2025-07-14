package com.coredisc.presentation.controller;

import com.coredisc.application.service.block.BlockCommandService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.BlockConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.BlockControllerDocs;
import com.coredisc.presentation.dto.block.BlockResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlockController implements BlockControllerDocs {

    private final BlockCommandService blockCommandService;

    @PostMapping("/api/block/{targetId}")
    public ApiResponse<BlockResponseDTO.BlockResultDTO> block(
            @CurrentMember Member member,
            @PathVariable Long targetId
    ) {

        return ApiResponse.onSuccess(BlockConverter.toBlockResultDTO(
                blockCommandService.block(member, targetId)
        ));
    }

    @DeleteMapping("/api/block/{targetId}")
    public ApiResponse<String> unblock(
            @CurrentMember Member member,
            @PathVariable Long targetId
    ) {

        blockCommandService.unblock(member, targetId);
        return ApiResponse.onSuccess("성공적으로 차단 취소되었습니다.");
    }
}
