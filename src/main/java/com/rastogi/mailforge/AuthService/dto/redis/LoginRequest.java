package com.rastogi.mailforge.AuthService.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String requestId;
    private String userId;

    private String requestingDeviceInfo;
    private String requestingIp;

    private String status; // PENDING, APPROVED, DENIED

    private String deviceIdentifier; // new device id

    private LocalDateTime createdAt;
}
