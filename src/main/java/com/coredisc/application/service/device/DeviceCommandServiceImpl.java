package com.coredisc.application.service.device;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.DeviceConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.domain.device.Device;
import com.coredisc.domain.device.DeviceRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.presentation.dto.device.DeviceRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;

    @Override
    // 사용자의 디바이스 토큰을 등록하거나 재활성화하는 기능(로그아웃했다가 로그인하는 과정을 고려)
    public Device registerDeviceToken(Member member, DeviceRequestDTO.registerDeviceDTO request) {
        return deviceRepository.findByMemberAndToken(member, request.getToken())
                .map(device -> {
                    // 해당 유저에게 동일한 토큰이 존재한다면 활성화시킴
                    device.updateActive(true);
                    return device;
                })
                .orElseGet(() -> {
                    // 만약 토큰이 존재하지 않다면 저장
                    Device device = DeviceConverter.toDevice(member, request.getToken(), request.getDeviceType());
                    return deviceRepository.save(device);
                });
    }

    @Override
    // 디바이스 토큰 비활성화 (로그아웃 경우)
    public void deactivateDeviceToken(String username, String token) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));

        deviceRepository.findByMemberAndToken(member, token)
                .ifPresent(device -> device.updateActive(false));
    }

    @Override
    // 디바이스 토큰들 삭제 (탈퇴 경우ㅇ)
    public void deleteDeviceToken(Member member) {
        deviceRepository.deleteAllByMember(member);
    }

}