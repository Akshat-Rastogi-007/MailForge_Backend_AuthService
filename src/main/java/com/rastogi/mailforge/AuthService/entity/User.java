package com.rastogi.mailforge.AuthService.entity;

import com.rastogi.mailforge.AuthService.enums.AccountStatus;
import com.rastogi.mailforge.AuthService.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true)
    private String username; // username
    @Column(nullable = false)
    private String password;

    private String externalMail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    private Set<Roles> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PostUpdate
    public void postUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
