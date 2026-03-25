package com.portfolio.virtualwallet.utils;

import java.math.BigDecimal;

public final class AppConstants {

    private AppConstants() {}

    public static final class Wallet {
        private Wallet() {}

        public static final String DEFAULT_WALLET_NAME = "Main Wallet";
        public static final BigDecimal INITIAL_BALANCE = BigDecimal.ZERO; // Вече имаме бизнес смисъл!
    }
}