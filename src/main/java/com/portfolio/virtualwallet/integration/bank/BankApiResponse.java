package com.portfolio.virtualwallet.integration.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankApiResponse {
    private boolean isSuccess;
    private String transactionReference;
    private String errorMessage;
}