package com.portfolio.virtualwallet.mapper;

import com.portfolio.virtualwallet.entity.VerificationToken;
import com.portfolio.virtualwallet.entity.dto.auth.UserRegisterDto;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.user.UserDetailsAdminDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private static final int EXPIRATION_HOURS = 24;

    public VerificationToken createVerificationToken(User user, String token) {
        return VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(java.time.LocalDateTime.now().plusHours(EXPIRATION_HOURS))
                .build();
    }

    public User toEntity(UserRegisterDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }

    public UserPublicResponseDto toPublicDto(User user) {
        return UserPublicResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .photoUrl(user.getPhotoUrl())
                .build();
    }

    public UserDetailsAdminDto toAdminDto(User user) {
        return UserDetailsAdminDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .isBlocked(user.isBlocked())
                .isEmailVerified(user.isEmailVerified())
                .build();
    }
}