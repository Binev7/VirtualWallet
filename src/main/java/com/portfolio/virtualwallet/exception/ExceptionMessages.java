package com.portfolio.virtualwallet.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {

    @UtilityClass
    public class User {
        public static final String USERNAME_ALREADY_EXISTS = "Username is already taken.";
        public static final String EMAIL_ALREADY_EXISTS = "Email is already in use.";
        public static final String PHONE_NUMBER_ALREADY_EXISTS = "Phone number is already in use.";
        public static final String USER_NOT_FOUND = "User not found.";
        public static final String INCORRECT_CURRENT_PASSWORD = "The current password you entered is incorrect.";
        public static final String PASSWORDS_DO_NOT_MATCH = "New passwords do not match.";
        public static final String USER_BLOCKED = "User account is blocked by an administrator.";
        public static final String ADMIN_PROTECTION = "Administrators cannot be blocked or unblocked!";
    }

    @UtilityClass
    public class Card {
        public static final String CARD_ALREADY_EXISTS = "Card with this number already exists.";
        public static final String CARD_NOT_FOUND = "Card not found.";
        public static final String CARD_NOT_OWNER = "You do not have permission to use this card.";
        public static final String STRIPE_PAYMENT_ERROR = "Invalid Stripe Payment Method.";
    }

    @UtilityClass
    public class Security {
        public static final String UNAUTHENTICATED = "User is not authenticated. Please log in.";
    }

    @UtilityClass
    public class System {
        public static final String UNEXPECTED_ERROR = "An unexpected error occurred.";
        public static final String UNEXPECTED_ERROR_LOG = "Unexpected error occurred: {}";
        public static final String VALIDATION_FAILED = "Validation failed";
    }

    @UtilityClass
    public class Wallet {
        public static final String WALLET_ALREADY_EXISTS = "You already have a wallet with this name.";
        public static final String WALLET_NOT_FOUND = "Wallet not found.";
        public static final String WALLET_ACCESS_DENIED = "You do not have permission to access this wallet.";
        public static final String WALLET_NOT_OWNER = "Only the wallet owner can perform this action.";
        public static final String WALLET_NON_EMPTY = "Cannot delete a wallet that has a positive balance. Please transfer the funds first.";
        public static final String WALLET_NOT_JOINT = "This is not a joint wallet. You cannot add members.";
        public static final String WALLET_USER_ALREADY_MEMBER = "This user is already a member of the wallet.";
        public static final String WALLET_CANNOT_REMOVE_OWNER = "The owner cannot be removed from the wallet.";
        public static final String WALLET_CANNOT_MODIFY_OWNER_RIGHTS = "You cannot modify the rights of the wallet owner.";
    }

    @UtilityClass
    public class Token {
        public static final String INVALID_TOKEN = "The verification token is invalid or does not exist.";
        public static final String EXPIRED_TOKEN = "The verification token has expired. Please request a new one.";
    }

    @UtilityClass
    public class Verification {
        public static final String EMAIL_ALREADY_VERIFIED = "This email is already verified.";
        public static final String UNVERIFIED_EMAIL_ACTION = "You must verify your email before performing this action.";
    }

    @UtilityClass
    public class Transaction {
        public static final String INSUFFICIENT_FUNDS = "Insufficient funds in the wallet to complete this transaction.";
        public static final String TRANSACTION_NOT_FOUND = "Transaction with the provided ID does not exist.";
        public static final String OTP_NOT_FOUND = "OTP not found for this transaction.";
        public static final String OTP_EXPIRED = "OTP code has expired. Transaction failed.";
        public static final String OTP_INVALID = "Invalid OTP code.";
        public static final String RECURRING_NOT_FOUND = "Recurring transaction not found.";
        public static final String NOT_OWNER_OF_RECURRING = "You are not authorized to cancel this recurring transaction.";
    }

    @UtilityClass
    public class Bank {
        public static final String BANK_REJECTED_TRANSACTION = "The bank rejected the transaction. Please check your card details or balance.";
        public static final String BANK_UNAVAILABLE = "The banking service is currently unavailable. Please try again later.";
        public static final String EXTERNAL_INSUFFICIENT_FUNDS = "Insufficient funds in the external bank account.";
        public static final String EXTERNAL_REJECTED_PAYOUT = "Bank rejected the payout request.";
    }
}