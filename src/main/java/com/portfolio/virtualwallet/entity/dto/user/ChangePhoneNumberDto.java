package com.portfolio.virtualwallet.entity.dto.user;

import com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePhoneNumberDto {

    @NotBlank(message = ValidationMessages.User.PHONE_NOT_BLANK)
    @Pattern(regexp = "^\\d{10}$", message = ValidationMessages.User.PHONE_PATTERN)
    private String newPhoneNumber;

}