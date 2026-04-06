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
    }

    public static final class Attributes {
        public static final String ERROR = "error";
        public static final String SUCCESS_MESSAGE = "successMessage";
        public static final String TOKEN = "token";
        public static final String CURRENT_USER = "currentUser";
        public static final String WALLET = "wallet";
        public static final String CARD = "card";
        public static final String RECENT_TRANSACTIONS = "recentTransactions";
    }

    public static final class Messages {
        public static final String REGISTRATION_SUCCESS = "Registration successful! Please check your email to verify your account.";
        public static final String RESET_LINK_SENT = "Password reset link has been sent to your email.";
        public static final String USER_NOT_FOUND = "User with this email not found.";
        public static final String RESET_SUCCESS = "Password has been reset successfully! You can now login.";
        public static final String INVALID_TOKEN = "Invalid or expired token.";
    }

    public static final class Cookies {
        public static final String JWT_COOKIE_NAME = "jwt_token";
        public static final String PATH = "/";
        public static final String SAME_SITE_STRICT = "Strict";
        public static final long MAX_AGE_SECONDS = 3600;
    }
}