package com.coredisc.application.service.device;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.device.DeviceResponseDTO;

import java.util.List;

public interface DeviceQueryService {

    List<DeviceResponseDTO.DeviceResultDTO> getDeviceTokens(Member member);
}
