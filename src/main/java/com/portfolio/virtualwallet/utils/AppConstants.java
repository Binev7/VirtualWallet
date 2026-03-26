package com.portfolio.virtualwallet.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

public final class AppConstants {

    private AppConstants() {}

    public static final class Wallet {
        private Wallet() {}

        public static final String DEFAULT_WALLET_NAME = "Main Wallet";
        public static final BigDecimal INITIAL_BALANCE = BigDecimal.ZERO; // Вече имаме бизнес смисъл!
    }

    @UtilityClass
    public class EmailVerification {
        public static final String VERIFY_EMAIL_ENDPOINT = "/api/v1/auth/verifyEmail?token=";
    }

    @UtilityClass
    public class Logging {
        public static final String REGISTRATION_SUCCESS = "Registration successful for user: {}. Verification link: {}";
    }
}