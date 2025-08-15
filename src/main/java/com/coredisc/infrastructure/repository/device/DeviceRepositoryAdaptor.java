package com.coredisc.infrastructure.repository.device;

import com.coredisc.domain.device.Device;
import com.coredisc.domain.device.DeviceRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeviceRepositoryAdaptor implements DeviceRepository {
    private final JpaDeviceRepository jpaDeviceRepository;

    @Override
    public Optional<Device> findByMemberAndToken(Member member, String token) {
        return jpaDeviceRepository.findByMemberAndToken(member, token);
    }

    @Override
    public Device save(Device device) {
        return jpaDeviceRepository.save(device);
    }

    @Override
    public void deleteAllByMember(Member member) {
        jpaDeviceRepository.deleteAllByMember(member);
    }

    @Override
    public List<Device> findAllByMember(Member member) {
        return jpaDeviceRepository.findAllByMember(member);
    }

    @Override
    public List<Device> findByMemberAndIsActiveTrue(Member member) {
        return jpaDeviceRepository.findByMemberAndIsActiveTrue(member);
    }
}
