package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.device.DeviceRequestDTO;
import com.coredisc.presentation.dto.device.DeviceResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Device", description = "디바이스 관련 API")
public interface DeviceControllerDocs {

    @Operation(summary = "디바이스 토큰 설정", description = "디바이스 토큰 설정 기능입니다.")
    ApiResponse<DeviceResponseDTO.DeviceResultDTO> registerDeviceToken(
            @CurrentMember Member member,
            @RequestBody DeviceRequestDTO.registerDeviceDTO request
    );

    @Operation(summary = "[테스트용] 디바이스 토큰 목록 조회", description = "유저의 디바이스 토큰 목록 조회 기능입니다.")
    ApiResponse<List<DeviceResponseDTO.DeviceResultDTO>> getDevices(@CurrentMember Member member);
}
