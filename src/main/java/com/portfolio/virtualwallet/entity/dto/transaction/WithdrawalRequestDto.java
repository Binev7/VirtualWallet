package com.portfolio.virtualwallet.entity.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.*;

@Data
public class WithdrawalRequestDto {

    @NotNull(message = WALLET_ID_NOT_NULL)
    private Long walletId;

    @NotNull(message = CARD_ID_NOT_NULL)
    private Long cardId;

    @NotNull(message = AMOUNT_NOT_NULL)
    @DecimalMin(value = "0.01", message = AMOUNT_MIN_VALUE)
    private BigDecimal amount;
}