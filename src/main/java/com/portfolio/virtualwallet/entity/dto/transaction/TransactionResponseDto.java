package com.portfolio.virtualwallet.entity.dto.transaction;

import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDto {
    private Long transactionId;
    private TransactionStatus status;
    private BigDecimal amount;
    private String message;
    private LocalDateTime timestamp;
}