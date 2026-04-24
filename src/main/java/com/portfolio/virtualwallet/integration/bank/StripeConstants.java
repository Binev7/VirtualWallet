package com.portfolio.virtualwallet.integration.bank;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class StripeConstants {
    public static final String CURRENCY = "usd";
    public static final String STATUS_SUCCEEDED = "succeeded";
    public static final String MSG_PAYMENT_FAILED = "Payment failed. Status: ";
    public static final String MSG_BANK_ERROR = "Bank integration error";
    public static final String PAYOUT_REF_PREFIX = "PAYOUT_";
}