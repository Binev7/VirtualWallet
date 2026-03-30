package com.portfolio.virtualwallet.integration.bank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankApiResponse {
    private boolean isSuccess;
    private String transactionReference;
    private String errorMessage;
}