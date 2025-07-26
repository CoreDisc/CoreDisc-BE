package com.coredisc.application.service.device;

import com.coredisc.domain.device.Device;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.device.DeviceRequestDTO;

public interface DeviceCommandService {

    Device registerDeviceToken(Member member, DeviceRequestDTO.registerDeviceDTO request);
}
