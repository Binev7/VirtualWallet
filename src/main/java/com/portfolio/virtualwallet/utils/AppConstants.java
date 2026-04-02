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
        public static final String PASSWORD_RESET_SUBJECT = "Password Reset Request";
        public static final String PASSWORD_RESET_TEMPLATE = "reset-password-email";
        public static final String RESET_TOKEN_VARIABLE = "resetToken";
        public static final String USERNAME_VARIABLE = "username";
    }

    @UtilityClass
    public class SuccessMessages {
        public static final String EMAIL_VERIFIED = "Email verified successfully! You can now use your Virtual Wallet.";
        public static final String TRANSFER_COMPLETED = "Transfer completed successfully.";
        public static final String OTP_SENT = "Verification OTP sent to your email.";
        public static final String RECURRING_CREATED_SUCCESS = "Recurring transaction successfully scheduled.";
        public static final String RECURRING_CANCELLED_SUCCESS = "Recurring transaction cancelled successfully.";
        public static final String DEPOSIT_COMPLETED = "Deposit completed successfully.";
        public static final String WITHDRAWAL_COMPLETED = "Withdrawal completed successfully.";
        public static final String FORGOT_PASSWORD_SUCCESS = "If an account with this email exists, a password reset link has been sent.";
        public static final String RESET_PASSWORD_SUCCESS = "Password has been successfully reset. You can now login.";
        public static final String EMAIL_CHANGE_SUCCESS = "Email changed successfully. Please check your new inbox for the verification link.";
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
        public static final String OWNER = "owner";
    }

    @UtilityClass
    public class User {
        public static final String USER_IS_BLOCKED = "Your account has been blocked due to suspicious activity. You cannot perform transactions.";
        public static final String USER_NOT_FOUND = "User not found.";
    }

    @UtilityClass
    public class Swagger {
        public static final String TITLE = "Virtual Wallet API";
        public static final String DESCRIPTION = "OpenAPI documentation for the Virtual Wallet Application";
        public static final String VERSION = "1.0";
        public static final String CONTACT_NAME = "Georgi Binev";
        public static final String CONTACT_URL = "https://github.com/Binev7";
        public static final String SECURITY_SCHEME_NAME = "bearerAuth";
        public static final String SECURITY_SCHEME_DESC = "JWT authentication. Put your token here (without the 'Bearer ' prefix).";
    }
}