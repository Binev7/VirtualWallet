package com.portfolio.virtualwallet.entity.dto.validation;

import com.portfolio.virtualwallet.entity.dto.auth.ResetPasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, ResetPasswordDto> {

    @Override
    public boolean isValid(ResetPasswordDto dto, ConstraintValidatorContext context) {
        if (dto.getNewPassword() == null || dto.getConfirmPassword() == null) {
            return false;
        }
        return dto.getNewPassword().equals(dto.getConfirmPassword());
    }
}