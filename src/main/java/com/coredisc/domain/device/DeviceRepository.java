package com.coredisc.domain.device;

import com.coredisc.domain.member.Member;

import java.util.Optional;

public interface DeviceRepository {

    Optional<Device> findByMemberAndToken(Member member, String token);
    Device save(Device device);
    void deleteAllByMember(Member member);
}
