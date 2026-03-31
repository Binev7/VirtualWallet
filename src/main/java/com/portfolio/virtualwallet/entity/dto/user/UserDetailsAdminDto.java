package com.portfolio.virtualwallet.entity.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDetailsAdminDto {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String role;
    private boolean isBlocked;
    private boolean isEmailVerified;
    private LocalDateTime createdAt;
}