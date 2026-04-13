package com.portfolio.virtualwallet.entity.dto.user;

import com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotBlank(message = ValidationMessages.Auth.CURRENT_PASSWORD_REQUIRED)
    private String currentPassword;

    @NotBlank(message = ValidationMessages.Auth.NEW_PASSWORD_REQUIRED)
    @Size(min = 8, message = ValidationMessages.Auth.PASSWORD_TOO_SHORT)
    private String newPassword;

    @NotBlank(message = ValidationMessages.Auth.CONFIRM_PASSWORD_REQUIRED)
    private String confirmPassword;

}