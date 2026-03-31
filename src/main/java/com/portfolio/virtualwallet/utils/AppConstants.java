package com.portfolio.virtualwallet.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

public final class AppConstants {

    private AppConstants() {}

    @UtilityClass
    public final class Wallet {
        public static final String DEFAULT_WALLET_NAME = "Main Wallet";
        public static final BigDecimal INITIAL_BALANCE = BigDecimal.ZERO;
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
        public static final String TRANSACTION_SUCCESS_SUBJECT = "Transaction Successful - Virtual Wallet";
        public static final String TRANSACTION_SUCCESS_TEMPLATE = "transaction-success-email";
        public static final String RECEIVED_MONEY_SUBJECT = "You've Received Money!";
        public static final String RECEIVED_MONEY_TEMPLATE = "received-money-email";
        public static final String AMOUNT_VARIABLE = "amount";
        public static final String RECIPIENT_VARIABLE = "recipient";
        public static final String SENDER_VARIABLE = "sender";
        public static final String RECURRING_FAILED_SUBJECT = "Action Required: Recurring Transaction Failed";
        public static final String RECURRING_FAILED_TEMPLATE = "recurring-failed-email";
        public static final String REASON_VARIABLE = "reason";
    }

    @UtilityClass
    public class SuccessMessages {
        public static final String EMAIL_VERIFIED = "Email verified successfully! You can now use your Virtual Wallet.";
        public static final String TRANSFER_COMPLETED = "Transfer completed successfully.";
        public static final String OTP_SENT = "Verification OTP sent to your email.";
        public static final String RECURRING_SETUP_SUCCESS = "Recurring transaction setup successfully.";
        public static final String RECURRING_CANCELLED = "Recurring transaction cancelled successfully.";
        public static final String DEPOSIT_COMPLETED = "Deposit completed successfully.";
        public static final String WITHDRAWAL_COMPLETED = "Withdrawal completed successfully.";
    }

    @UtilityClass
    public class Bank {
        public static final String DEPOSIT_REF_PREFIX = "BANK-REF-";
        public static final String PAYOUT_REF_PREFIX = "PAYOUT-REF-";
    }

    @UtilityClass
    public class Pagination {
        public static final String DEFAULT_PAGE_NUMBER = "0";
        public static final String DEFAULT_PAGE_SIZE = "10";
    }

    @UtilityClass
    public class History {
        public static final String INCOMING_DIRECTION = "INCOMING";
        public static final String OUTGOING_DIRECTION = "OUTGOING";
        public static final String EXTERNAL_BANK = "External Bank";
        public static final int DEFAULT_HISTORY_MONTHS = 1;
    }

    @UtilityClass
    public class EntityFields {
        public static final String CREATED_AT = "createdAt";
        public static final String SENDER_WALLET = "senderWallet";
        public static final String RECEIVER_WALLET = "receiverWallet";
        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String STATUS = "status";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String PHONE_NUMBER = "phoneNumber";
    }

    @UtilityClass
    public class User {
        public static final String USER_IS_BLOCKED = "Your account has been blocked due to suspicious activity. You cannot perform transactions.";
        public static final String USER_NOT_FOUND = "User not found.";
    }
}