package com.coredisc.presentation.dto.device;

import lombok.Getter;

public class DeviceRequestDTO {

    @Getter
    public static class registerDeviceDTO {
        private String token;
        private String deviceType;
    }
}
