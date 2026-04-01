package com.portfolio.virtualwallet.entity.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.EMAIL_REQUIRED;
import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.INVALID_EMAIL_FORMAT;

@Data
public class ForgotPasswordDto {

    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = INVALID_EMAIL_FORMAT)
    private String email;
}