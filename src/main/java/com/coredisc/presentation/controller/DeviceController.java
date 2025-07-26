package com.coredisc.presentation.controller;

import com.coredisc.application.service.device.DeviceCommandService;
import com.coredisc.application.service.device.DeviceQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.DeviceConverter;
import com.coredisc.domain.device.DeviceRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.DeviceControllerDocs;
import com.coredisc.presentation.dto.device.DeviceRequestDTO;
import com.coredisc.presentation.dto.device.DeviceResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeviceController implements DeviceControllerDocs {

    private final DeviceCommandService deviceCommandService;
    private final DeviceRepository deviceRepository;
    private final DeviceQueryService deviceQueryService;

    @PostMapping("/api/device-token")
    public ApiResponse<DeviceResponseDTO.DeviceResultDTO> registerDeviceToken(
            @CurrentMember Member member,
            @RequestBody DeviceRequestDTO.registerDeviceDTO request
    ){
        return ApiResponse.onSuccess(DeviceConverter.deviceResultDTO(
                deviceCommandService.registerDeviceToken(member, request)
        ));
    }

    @GetMapping("/api/device-tokens")
    public ApiResponse<List<DeviceResponseDTO.DeviceResultDTO>> getDevices(@CurrentMember Member member) {
        return ApiResponse.onSuccess(deviceQueryService.getDeviceTokens(member));
    }

}
