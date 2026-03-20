package com.portfolio.virtualwallet.entity.dto.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessages {

    public final String USERNAME_NOT_BLANK = "Username cannot be empty";
    public final String USERNAME_SIZE = "Username must be between 2 and 20 symbols.";

    public final String PASSWORD_NOT_BLANK = "Password cannot be empty";
    public final String PASSWORD_PATTERN = "Password must be at least 8 characters and contain a capital letter, a digit, and a special symbol (+, -, *, &, ^).";

    public final String EMAIL_NOT_BLANK = "Email cannot be empty";
    public final String EMAIL_INVALID = "Email must be valid.";

    public final String PHONE_NOT_BLANK = "Phone number cannot be empty";
    public final String PHONE_PATTERN = "Phone number must be exactly 10 digits.";
}