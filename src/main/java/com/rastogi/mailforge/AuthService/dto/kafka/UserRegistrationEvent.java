package com.rastogi.mailforge.AuthService.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Kafka event published when a user registers in Auth Service
 * Consumed by User Service to create user profile and mail configuration
 * Topic: auth.user.registration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private String externalMail;
    private String status; // AccountStatus
    private Long createdAt; // timestamp in milliseconds
}
