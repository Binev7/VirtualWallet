package com.portfolio.virtualwallet.integration.bank;

import com.portfolio.virtualwallet.entity.Card;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

import static com.portfolio.virtualwallet.integration.bank.StripeConstants.*;

@Component
@Slf4j
public class StripeGatewayClientImpl implements BankingGatewayClient {

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public BankApiResponse processDeposit(Card card, BigDecimal amount) {
        log.info("Processing Stripe deposit for card PM ID: {}", card.getStripePaymentMethodId());

        try {
            long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(CURRENCY)
                    .setPaymentMethod(card.getStripePaymentMethodId())
                    .setConfirm(true)
                    .setOffSession(true)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            if (STATUS_SUCCEEDED.equals(intent.getStatus())) {
                log.info("Stripe payment successful! Ref: {}", intent.getId());
                return BankApiResponse.builder()
                        .isSuccess(true)
                        .transactionReference(intent.getId())
                        .build();
            } else {
                log.warn("Stripe payment failed with status: {}", intent.getStatus());
                return BankApiResponse.builder()
                        .isSuccess(false)
                        .errorMessage(MSG_PAYMENT_FAILED + intent.getStatus())
                        .build();
            }

        } catch (StripeException e) {
            log.error("Stripe API error: {}", e.getMessage());
            return BankApiResponse.builder()
                    .isSuccess(false)
                    .errorMessage(e.getUserMessage() != null ? e.getUserMessage() : MSG_BANK_ERROR)
                    .build();
        }
    }

    @Override
    public BankApiResponse processWithdrawal(Card card, BigDecimal amount) {
        log.info("Simulating successful withdrawal to card PM ID: {}", card.getStripePaymentMethodId());

        return BankApiResponse.builder()
                .isSuccess(true)
                .transactionReference(PAYOUT_REF_PREFIX + UUID.randomUUID())
                .build();
    }
}