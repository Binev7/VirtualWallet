package com.portfolio.virtualwallet.controller.mvc.constants;

public final class MvcConstants {

    private MvcConstants() {}

    public static final class Views {
        public static final String LOGIN = "auth/login";
        public static final String REGISTER = "auth/register";
        public static final String FORGOT_PASSWORD = "auth/forgot-password";
        public static final String RESET_PASSWORD = "auth/reset-password";
        public static final String REDIRECT_HOME = "redirect:/";
        public static final String DASHBOARD_INDEX = "dashboard/index";
        public static final String ADD_CARD = "cards/add-card";
        public static final String TOP_UP = "funding/top-up";
        public static final String MY_CARDS = "cards/my-cards";
        public static final String EDIT_CARD = "cards/edit-card";
        public static final String REDIRECT_MY_CARDS = "redirect:/cards";
        public static final String WITHDRAW = "funding/withdraw";
    }

    public static final class Attributes {
        public static final String ERROR = "error";
        public static final String SUCCESS_MESSAGE = "successMessage";
        public static final String TOKEN = "token";
        public static final String CURRENT_USER = "currentUser";
        public static final String WALLET = "wallet";
        public static final String CARD = "card";
        public static final String RECENT_TRANSACTIONS = "recentTransactions";
        public static final String CARDS = "cards";
        public static final String WALLETS = "wallets";
        public static final String DEPOSIT_REQUEST = "depositRequest";
        public static final String ALL_RECENT_TRANSACTIONS = "allRecentTransactions";
        public static final String WITHDRAWAL_REQUEST = "withdrawalRequest";
    }

    public static final class Messages {
        public static final String REGISTRATION_SUCCESS = "Registration successful! Please check your email to verify your account.";
        public static final String RESET_LINK_SENT = "Password reset link has been sent to your email.";
        public static final String USER_NOT_FOUND = "User with this email not found.";
        public static final String RESET_SUCCESS = "Password has been reset successfully! You can now login.";
        public static final String INVALID_TOKEN = "Invalid or expired token.";
        public static final String TOP_UP_SUCCESS = "Successfully added funds to your wallet!";
        public static final String CARD_ADDED_SUCCESS = "Card successfully added to your account!";
        public static final String CARD_UPDATED_SUCCESS = "Card successfully updated!";
        public static final String CARD_DELETED_SUCCESS = "Card successfully removed!";
        public static final String WITHDRAW_SUCCESS = "Funds successfully withdrawn to your card!";
    }

    public static final class Cookies {
        public static final String JWT_COOKIE_NAME = "jwt_token";
        public static final String PATH = "/";
        public static final String SAME_SITE_STRICT = "Strict";
        public static final long MAX_AGE_SECONDS = 3600;
    }
}