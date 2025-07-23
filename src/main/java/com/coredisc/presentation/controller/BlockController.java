package com.coredisc.presentation.controller;

import com.coredisc.application.service.block.BlockCommandService;
import com.coredisc.application.service.block.BlockQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.BlockConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.BlockControllerDocs;
import com.coredisc.presentation.dto.block.BlockResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BlockController implements BlockControllerDocs {

    private final BlockCommandService blockCommandService;
    private final BlockQueryService blockQueryService;

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

    @GetMapping("/api/blocks")
    public ApiResponse<CursorDTO<BlockResponseDTO.BlockedDTO>> getBlockedList(
            @CurrentMember Member member,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(0, size);
        return ApiResponse.onSuccess(blockQueryService.getBlockedList(member, cursorId, pageable));
    }
}
