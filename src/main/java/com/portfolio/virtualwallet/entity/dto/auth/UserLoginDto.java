package com.portfolio.virtualwallet.entity.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.portfolio.virtualwallet.entity.dto.auth.constants.ValidationMessages.*;

@Data
public class UserLoginDto {

    @NotBlank(message = USERNAME_NOT_BLANK)
    private String username;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;
}