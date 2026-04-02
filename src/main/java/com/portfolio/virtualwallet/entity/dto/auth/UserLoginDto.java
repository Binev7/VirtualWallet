package com.portfolio.virtualwallet.entity.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.User.*;

@Data
public class UserLoginDto {

    @NotBlank(message = EMAIL_NOT_BLANK)
    @Email(message = EMAIL_INVALID)
    private String email;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;
}