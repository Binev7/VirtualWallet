package com.portfolio.virtualwallet.entity.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Auth.EMAIL_REQUIRED;
import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Auth.INVALID_EMAIL_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordDto {

    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = INVALID_EMAIL_FORMAT)
    private String email;
}