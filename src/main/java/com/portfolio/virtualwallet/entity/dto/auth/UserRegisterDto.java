package com.portfolio.virtualwallet.entity.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.portfolio.virtualwallet.entity.dto.auth.constants.ValidationMessages.*;

@Data
public class UserRegisterDto {

    @NotBlank(message = USERNAME_NOT_BLANK)
    @Size(min = 2, max = 20, message = USERNAME_SIZE)
    private String username;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[+\\-*&^]).{8,}$", message = PASSWORD_PATTERN)
    private String password;

    @NotBlank(message = EMAIL_NOT_BLANK)
    @Email(message = EMAIL_INVALID)
    private String email;

    @NotBlank(message = PHONE_NOT_BLANK)
    @Pattern(regexp = "^\\d{10}$", message = PHONE_PATTERN)
    private String phoneNumber;
}