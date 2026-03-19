package com.portfolio.virtualwallet.entity.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDto {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols.")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[+\\-*&^]).{8,}$",
            message = "Password must be at least 8 characters and contain a capital letter, a digit, and a special symbol (+, -, *, &, ^).")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits.")
    private String phoneNumber;
}