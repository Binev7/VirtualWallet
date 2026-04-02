package com.portfolio.virtualwallet.entity.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Transaction.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {

    @NotNull(message = SENDER_WALLET_ID_NOT_NULL)
    private Long senderWalletId;

    @NotNull(message = RECEIVER_WALLET_ID_NOT_NULL)
    private Long receiverWalletId;

    @NotNull(message = AMOUNT_NOT_NULL)
    @DecimalMin(value = "0.01", message = AMOUNT_MIN_VALUE)
    private BigDecimal amount;
}
