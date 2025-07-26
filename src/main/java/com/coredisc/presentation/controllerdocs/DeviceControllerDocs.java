package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.device.DeviceRequestDTO;
import com.coredisc.presentation.dto.device.DeviceResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Device", description = "디바이스 관련 API")
public interface DeviceControllerDocs {

    @Operation(summary = "디바이스 토큰 설정", description = "디바이스 토큰 설정 기능입니다.")
    ApiResponse<DeviceResponseDTO.DeviceResultDTO> registerDeviceToken(
            @CurrentMember Member member,
            @RequestBody DeviceRequestDTO.registerDeviceDTO request
    );

}
