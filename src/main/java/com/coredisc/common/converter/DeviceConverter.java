package com.coredisc.common.converter;

import com.coredisc.domain.device.Device;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.device.DeviceResponseDTO;

public class DeviceConverter {

    public static Device toDevice(Member member, String token, String deviceType) {
        return Device.builder()
                .member(member)
                .token(token)
                .deviceType(deviceType != null ? deviceType : "iOS")
                .isActive(true)
                .build();
    }

    public static DeviceResponseDTO.DeviceResultDTO deviceResultDTO(Device device) {
        return DeviceResponseDTO.DeviceResultDTO.builder()
                .id(device.getId())
                .token(device.getToken())
                .deviceType(device.getDeviceType())
                .isActive(device.isActive())
                .createdAt(device.getCreatedAt())
                .build();
    }
}
