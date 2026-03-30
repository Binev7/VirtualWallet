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
        public static final String EMAIL_SEND_SUCCESS = "Verification email sent successfully to: {}";
        public static final String EMAIL_SEND_ERROR = "Failed to send verification email to: {}";
    }

    @UtilityClass
    public class Email {
        public static final String VERIFICATION_SUBJECT = "Verify Your Virtual Wallet Account";
        public static final String VERIFICATION_TEMPLATE = "verification-email";
        public static final String URL_VARIABLE = "confirmationUrl";
        public static final String OTP_SUBJECT = "Action Required: Verify Your Transaction";
        public static final String OTP_TEMPLATE = "otp-email";
        public static final String OTP_VARIABLE = "otpCode";
    }

    @UtilityClass
    public class SuccessMessages {
        public static final String EMAIL_VERIFIED = "Email verified successfully! You can now use your Virtual Wallet.";
        public static final String TRANSFER_COMPLETED = "Transfer completed successfully.";
        public static final String OTP_SENT = "Verification OTP sent to your email.";
    }
}