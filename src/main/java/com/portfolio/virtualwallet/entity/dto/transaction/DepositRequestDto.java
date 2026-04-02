package com.portfolio.virtualwallet.entity.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;


import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Card.*;
import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Transaction.*;


@Data
public class DepositRequestDto {

    @NotNull(message = WALLET_ID_NOT_NULL)
    private Long walletId;

    @NotNull(message = CARD_ID_NOT_NULL)
    private Long cardId;

    @NotNull(message = AMOUNT_NOT_NULL)
    @DecimalMin(value = "0.01", message = AMOUNT_MIN_VALUE)
    private BigDecimal amount;

    @NotBlank(message = CVV_NOT_BLANK)
    @Pattern(regexp = "^[0-9]{3}$", message = CVV_PATTERN)
    private String cvv;
}