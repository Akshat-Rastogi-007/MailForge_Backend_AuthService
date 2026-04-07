package com.rastogi.mailforge.AuthService.entity;

import com.rastogi.mailforge.AuthService.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class LoginAttempts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    @Column(nullable = false)
    private String requestingDeviceInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String jwtToken;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        expiresAt = LocalDateTime.now().plusMinutes(2);
        status = Status.PENDING;
    }

}
