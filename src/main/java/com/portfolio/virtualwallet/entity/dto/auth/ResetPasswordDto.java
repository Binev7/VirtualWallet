package com.portfolio.virtualwallet.entity.dto.auth;

import com.portfolio.virtualwallet.entity.dto.validation.PasswordsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.*;

@Data
@PasswordsMatch(message = PASSWORDS_DO_NOT_MATCH)
public class ResetPasswordDto {

    @NotBlank(message = TOKEN_REQUIRED)
    private String token;

    @NotBlank(message = NEW_PASSWORD_REQUIRED)
    @Size(min = 8, message = PASSWORD_TOO_SHORT)
    private String newPassword;

    @NotBlank(message = CONFIRM_PASSWORD_REQUIRED)
    private String confirmPassword;
}