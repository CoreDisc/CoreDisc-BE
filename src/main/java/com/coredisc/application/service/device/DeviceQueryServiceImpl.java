package com.coredisc.application.service.device;

import com.coredisc.common.converter.DeviceConverter;
import com.coredisc.domain.device.DeviceRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.device.DeviceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository deviceRepository;

    @Override
    public List<DeviceResponseDTO.DeviceResultDTO> getDeviceTokens(Member member) {
        return deviceRepository.findAllByMember(member).stream()
                .map(DeviceConverter::deviceResultDTO)
                .toList();
    }
}
