package com.coredisc.presentation.controller;

import com.coredisc.application.service.device.DeviceCommandService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.DeviceConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.DeviceControllerDocs;
import com.coredisc.presentation.dto.device.DeviceRequestDTO;
import com.coredisc.presentation.dto.device.DeviceResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeviceController implements DeviceControllerDocs {

    private final DeviceCommandService deviceCommandService;

    @PostMapping("/api/device-token")
    public ApiResponse<DeviceResponseDTO.DeviceResultDTO> registerDeviceToken(
            @CurrentMember Member member,
            @RequestBody DeviceRequestDTO.registerDeviceDTO request
    ){
        return ApiResponse.onSuccess(DeviceConverter.deviceResultDTO(
                deviceCommandService.registerDeviceToken(member, request)
        ));
    }
}
