package com.portfolio.virtualwallet.entity.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPublicResponseDto {
    private Long id;
    private String username;
    private String photoUrl;
    private String firstName;
}