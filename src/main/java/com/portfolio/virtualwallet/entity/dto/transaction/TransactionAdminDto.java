package com.portfolio.virtualwallet.entity.dto.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionAdminDto {
    private Long transactionId;
    private BigDecimal amount;
    private String type;
    private String status;
    private LocalDateTime date;
    private String senderUsername;
    private String receiverUsername;
}