package com.portfolio.virtualwallet.entity.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Auth.*;


@Data
public class ChangeEmailDto {

    @NotBlank(message = NEW_EMAIL_REQUIRED)
    @Email(message = INVALID_EMAIL_FORMAT)
    private String newEmail;
}