package com.portfolio.virtualwallet.entity.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Transaction.*;
import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Card.CARD_ID_NOT_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalRequestDto {

    @NotNull(message = WALLET_ID_NOT_NULL)
    private Long walletId;

    @NotNull(message = CARD_ID_NOT_NULL)
    private Long cardId;

    @NotNull(message = AMOUNT_NOT_NULL)
    @DecimalMin(value = "0.01", message = AMOUNT_MIN_VALUE)
    private BigDecimal amount;
}