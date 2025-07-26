package com.coredisc.application.service.device;

import com.coredisc.domain.device.Device;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.device.DeviceRequestDTO;

public interface DeviceCommandService {

    Device registerDeviceToken(Member member, DeviceRequestDTO.registerDeviceDTO request);

    // 디바이스 토큰 비활성화 시키기
    void deactivateDeviceToken(String username, String token);

    void deleteDeviceToken(Member member);
}
