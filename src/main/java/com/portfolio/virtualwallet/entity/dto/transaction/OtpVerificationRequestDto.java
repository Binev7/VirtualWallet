package com.portfolio.virtualwallet.entity.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.OTP_CODE_NOT_BLANK;
import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.TRANSACTION_ID_NOT_NULL;

@Data
public class OtpVerificationRequestDto {

    @NotNull(message = TRANSACTION_ID_NOT_NULL)
    private Long transactionId;

    @NotBlank(message = OTP_CODE_NOT_BLANK)
    private String otpCode;
}