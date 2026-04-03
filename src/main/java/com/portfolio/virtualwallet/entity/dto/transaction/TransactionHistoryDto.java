package com.portfolio.virtualwallet.entity.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryDto {
    private Long transactionId;
    private BigDecimal amount;
    private String type;
    private String status;
    private LocalDateTime date;
    private String counterparty;
    private String direction;
}