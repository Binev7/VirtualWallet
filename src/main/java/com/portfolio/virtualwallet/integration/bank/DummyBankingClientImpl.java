package com.portfolio.virtualwallet.integration.bank;

import com.portfolio.virtualwallet.entity.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.Bank.EXTERNAL_INSUFFICIENT_FUNDS;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.Bank.EXTERNAL_REJECTED_PAYOUT;
import static com.portfolio.virtualwallet.utils.AppConstants.Bank.DEPOSIT_REF_PREFIX;
import static com.portfolio.virtualwallet.utils.AppConstants.Bank.PAYOUT_REF_PREFIX;

@Component
@Slf4j
public class DummyBankingClientImpl implements BankingGatewayClient {

    @Value("${app.bank.dummy.deposit-delay-ms:1000}")
    private long depositDelayMs;

    @Value("${app.bank.dummy.withdrawal-delay-ms:1500}")
    private long withdrawalDelayMs;

    @Value("${app.bank.dummy.success-rate-deposit:0.1}")
    private double depositFailureRate;

    @Value("${app.bank.dummy.success-rate-withdrawal:0.05}")
    private double withdrawalFailureRate;

    @Override
    public BankApiResponse processDeposit(Card card, String cvv, BigDecimal amount) {
        log.info("Sending HTTP POST request to Dummy Bank API for Card: ****{}",
                card.getCardNumber().substring(card.getCardNumber().length() - 4));

        simulateNetworkDelay(depositDelayMs);

        boolean isBankSuccess = Math.random() > depositFailureRate;

        if (isBankSuccess) {
            log.info("Bank API responded with 200 OK");
            return BankApiResponse.builder()
                    .isSuccess(true)
                    .transactionReference(DEPOSIT_REF_PREFIX + UUID.randomUUID())
                    .build();
        } else {
            log.warn("Bank API responded with 400 Bad Request (Insufficient Funds)");
            return BankApiResponse.builder()
                    .isSuccess(false)
                    .errorMessage(EXTERNAL_INSUFFICIENT_FUNDS)
                    .build();
        }
    }

    @Override
    public BankApiResponse processWithdrawal(Card card, BigDecimal amount) {
        log.info("Sending HTTP POST request to Dummy Bank API to transfer funds TO Card: ****{}",
                card.getCardNumber().substring(card.getCardNumber().length() - 4));

        simulateNetworkDelay(withdrawalDelayMs);

        boolean isBankSuccess = Math.random() > withdrawalFailureRate;

        if (isBankSuccess) {
            return BankApiResponse.builder()
                    .isSuccess(true)
                    .transactionReference(PAYOUT_REF_PREFIX + UUID.randomUUID())
                    .build();
        } else {
            return BankApiResponse.builder()
                    .isSuccess(false)
                    .errorMessage(EXTERNAL_REJECTED_PAYOUT)
                    .build();
        }
    }

    private void simulateNetworkDelay(long delayMs) {
        if (delayMs > 0) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}