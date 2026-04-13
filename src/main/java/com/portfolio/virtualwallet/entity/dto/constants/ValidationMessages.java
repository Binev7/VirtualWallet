package com.portfolio.virtualwallet.entity.dto.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessages {

    @UtilityClass
    public static class User {
        public static final String USERNAME_NOT_BLANK = "Username cannot be empty";
        public static final String USERNAME_SIZE = "Username must be between 2 and 20 symbols.";
        public static final String PASSWORD_NOT_BLANK = "Password cannot be empty";
        public static final String PASSWORD_PATTERN = "Password must be at least 8 characters and contain a capital letter, a digit, and a special symbol (+, -, *, &, ^).";
        public static final String EMAIL_NOT_BLANK = "Email cannot be empty";
        public static final String EMAIL_INVALID = "Email must be valid.";
        public static final String PHONE_NOT_BLANK = "Phone number cannot be empty";
        public static final String PHONE_PATTERN = "Phone number must be exactly 10 digits.";
    }

    @UtilityClass
    public static class Wallet {
        public static final String WALLET_NAME_NOT_BLANK = "Wallet name cannot be empty.";
        public static final String WALLET_NAME_SIZE = "Wallet name must be between 2 and 30 characters.";
    }

    @UtilityClass
    public static class Card {
        public static final String CARD_ID_NOT_NULL = "Card ID is required.";
        public static final String CARD_NUMBER_NOT_BLANK = "Card number cannot be empty.";
        public static final String CARD_NUMBER_PATTERN = "Card number must be exactly 16 digits.";
        public static final String EXPIRATION_DATE_NOT_NULL = "Expiration date is required.";
        public static final String EXPIRATION_DATE_FUTURE = "Expiration date must be in the future.";
        public static final String CARD_HOLDER_NOT_BLANK = "Card holder name cannot be empty.";
        public static final String CARD_HOLDER_SIZE = "Card holder must be between 2 and 30 symbols.";
        public static final String CHECK_NUMBER_NOT_BLANK = "Check number (CVV) cannot be empty.";
        public static final String CHECK_NUMBER_PATTERN = "Check number must be exactly 3 digits.";
        public static final String CVV_NOT_BLANK = "CVV code is required.";
        public static final String CVV_PATTERN = "CVV must be exactly 3 digits.";
    }

    @UtilityClass
    public static class Transaction {
        public static final String SENDER_WALLET_ID_NOT_NULL = "Sender wallet ID is required.";
        public static final String RECEIVER_WALLET_ID_NOT_NULL = "Receiver wallet ID is required.";
        public static final String WALLET_ID_NOT_NULL = "Wallet ID is required.";
        public static final String AMOUNT_NOT_NULL = "Amount is required.";
        public static final String AMOUNT_MIN_VALUE = "Transfer amount must be greater than zero.";
        public static final String INTERVAL_NOT_NULL = "Recurring interval is required.";
        public static final String TRANSACTION_ID_NOT_NULL = "Transaction ID is required.";
        public static final String OTP_CODE_NOT_BLANK = "OTP code cannot be empty.";
    }

    @UtilityClass
    public static class Auth {
        public static final String EMAIL_REQUIRED = "Email is required";
        public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
        public static final String TOKEN_REQUIRED = "Token is required";
        public static final String NEW_PASSWORD_REQUIRED = "New password is required";
        public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters long";
        public static final String NEW_EMAIL_REQUIRED = "New email is required";
        public static final String CONFIRM_PASSWORD_REQUIRED = "Confirm password is required";
        public static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
        public static final String CURRENT_PASSWORD_REQUIRED = "Current password is required";
    }
}