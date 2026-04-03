package com.portfolio.virtualwallet.entity.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Transaction.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerificationRequestDto {

    @NotNull(message = TRANSACTION_ID_NOT_NULL)
    private Long transactionId;

    @NotBlank(message = OTP_CODE_NOT_BLANK)
    private String otpCode;
}