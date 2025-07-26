package com.coredisc.infrastructure.repository.device;

import com.coredisc.domain.device.Device;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaDeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByMemberAndToken(Member member, String token);
    List<Device> findAllByMember(Member member);
}
