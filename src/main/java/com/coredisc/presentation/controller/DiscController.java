package com.coredisc.presentation.controller;

import com.coredisc.application.service.disc.DiscCommandService;
import com.coredisc.application.service.disc.DiscQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.DiscConverter;
import com.coredisc.common.exception.handler.DiscHandler;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.DiscControllerDocs;
import com.coredisc.presentation.dto.disc.DiscRequestDTO;
import com.coredisc.presentation.dto.disc.DiscResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports/discs")
public class DiscController implements DiscControllerDocs {

    private final DiscQueryService discQueryService;
    private final DiscCommandService discCommandService;

    private static final int PAGE_SIZE = 15;

    //나의 디스크 목록 조회
    @GetMapping
    public ApiResponse<DiscResponseDTO.DiscListDTO> getDiscList(@RequestParam(name = "page") int page, @CurrentMember Member member) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("year").descending().and(Sort.by("month").descending()));
        return ApiResponse.onSuccess(DiscConverter.toDiscListDTO(discQueryService.getMyDiscList(member, pageable)));
    }

    //id로 디스크 조회
    @GetMapping("/{discId}")
    public ApiResponse<DiscResponseDTO.DiscDTO> getDisc(@PathVariable(name = "discId") Long discId, @CurrentMember Member member) {
        return ApiResponse.onSuccess(DiscConverter.toDiscDTO(discQueryService.getDisc(discId, member)));
    }

    //디스크 커버 이미지 변경
    @PatchMapping("/{discId}/cover/image")
    public ApiResponse<DiscResponseDTO.DiscDTO> updateDiscCoverImage(@PathVariable(name = "discId") Long discId, @Valid @RequestBody DiscRequestDTO.UpdateCoverImgDTO request, @CurrentMember Member member) {
        return ApiResponse.onSuccess(DiscConverter.toDiscDTO(discCommandService.updateDiscCoverImage(discId, request.getCoverImageUrl(), member)));
    }

    //디스크 커버 색깔 변경
    @PatchMapping("/{discId}/cover/color")
    public ApiResponse<DiscResponseDTO.DiscDTO> updateDiscCoverColor(@PathVariable(name = "discId") Long discId, @Valid @RequestBody DiscRequestDTO.UpdateCoverColorDTO request, @CurrentMember Member member) {
        return ApiResponse.onSuccess(DiscConverter.toDiscDTO(discCommandService.updateDiscCoverColor(discId, request.getCoverColor(), member)));
    }
}
