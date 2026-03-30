package com.portfolio.virtualwallet.integration.bank;

import com.portfolio.virtualwallet.entity.Card;

import java.math.BigDecimal;

public interface BankingGatewayClient {
    BankApiResponse processDeposit(Card card, String cvv, BigDecimal amount);
    BankApiResponse processWithdrawal(Card card, BigDecimal amount);
}
