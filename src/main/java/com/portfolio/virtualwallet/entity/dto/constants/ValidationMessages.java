package com.portfolio.virtualwallet.entity.dto.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessages {

    // --- USERNAME VALIDATION ---
    public final String USERNAME_NOT_BLANK = "Username cannot be empty";
    public final String USERNAME_SIZE = "Username must be between 2 and 20 symbols.";

    // --- PASSWORD VALIDATION ---
    public final String PASSWORD_NOT_BLANK = "Password cannot be empty";
    public final String PASSWORD_PATTERN = "Password must be at least 8 characters and contain a capital letter, a digit, and a special symbol (+, -, *, &, ^).";

    // --- EMAIL VALIDATION ---
    public final String EMAIL_NOT_BLANK = "Email cannot be empty";
    public final String EMAIL_INVALID = "Email must be valid.";

    // --- PHONE VALIDATION ---
    public final String PHONE_NOT_BLANK = "Phone number cannot be empty";
    public final String PHONE_PATTERN = "Phone number must be exactly 10 digits.";

    // --- WALLET VALIDATION ---
    public final String WALLET_NAME_NOT_BLANK = "Wallet name cannot be empty.";
    public final String WALLET_NAME_SIZE = "Wallet name must be between 2 and 30 characters.";

    // --- TRANSACTION VALIDATION ---
    public static final String SENDER_WALLET_ID_NOT_NULL = "Sender wallet ID is required.";
    public static final String RECEIVER_WALLET_ID_NOT_NULL = "Receiver wallet ID is required.";
    public static final String AMOUNT_NOT_NULL = "Amount is required.";
    public static final String AMOUNT_MIN_VALUE = "Transfer amount must be greater than zero.";
    public static final String INTERVAL_NOT_NULL = "Recurring interval is required.";
}