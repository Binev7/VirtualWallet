package com.portfolio.virtualwallet.controller.mvc.constants;

public final class MvcConstants {

    private MvcConstants() {}

    public static final class Paths {
        public static final String REDIRECT = "redirect:";
        public static final String WALLETS = "/wallets";
        public static final String MEMBERS = "/members";
        public static final String DELETE = "/delete";
        public static final String ADD = "/add";
        public static final String PROFILE = "/profile";
        public static final String TRANSACTIONS = "/transactions";
        public static final String ADMIN_USERS = "/admin/users";
        public static final String ADMIN_TRANSACTIONS = "/admin/transactions";
        public static final String ADMIN_DASHBOARD = "/admin";
    }

    public static String getWalletMembersRedirect(Long walletId) {
        return Paths.REDIRECT + Paths.WALLETS + "/" + walletId + Paths.MEMBERS;
    }

    public static String getBindingResultKey(String attributeName) {
        return Attributes.BINDING_RESULT_PREFIX + attributeName;
    }

    public static final class Views {
        public static final String LOGIN = "auth/login";
        public static final String REGISTER = "auth/register";
        public static final String FORGOT_PASSWORD = "auth/forgot-password";
        public static final String RESET_PASSWORD = "auth/reset-password";
        public static final String REDIRECT_HOME = "redirect:/";
        public static final String REDIRECT_RECURRING = "redirect:/recurring";
        public static final String DASHBOARD_INDEX = "dashboard/index";
        public static final String ADD_CARD = "cards/add-card";
        public static final String TOP_UP = "funding/top-up";
        public static final String MY_CARDS = "cards/my-cards";
        public static final String EDIT_CARD = "cards/edit-card";
        public static final String REDIRECT_MY_CARDS = "redirect:/cards";
        public static final String WITHDRAW = "funding/withdraw";
        public static final String TRANSFER_SEARCH = "transfer/search";
        public static final String TRANSFER_FORM = "transfer/form";
        public static final String TRANSFER_OTP = "transfer/otp";
        public static final String RECURRING_FORM = "recurring/form";
        public static final String RECURRING_SEARCH = "recurring/search";
        public static final String RECURRING_LIST = "recurring/list";
        public static final String WALLETS_LIST = "wallets/list";
        public static final String WALLET_FORM = "wallets/form";
        public static final String WALLET_MEMBERS = "wallets/members";
        public static final String REDIRECT_WALLETS = "redirect:/wallets";
        public static final String REDIRECT_LOGIN = "redirect:/login";
        public static final String PROFILE = "user/profile";
        public static final String REDIRECT_PROFILE = "redirect:/profile";
        public static final String TRANSACTIONS_HISTORY = "transactions/history";
        public static final String ADMIN_USERS_LIST = "admin/users";
        public static final String REDIRECT_ADMIN_USERS = "redirect:/admin/users";
        public static final String ADMIN_TRANSACTIONS_LIST = "admin/transactions";
        public static final String ADMIN_DASHBOARD_VIEW = "admin/dashboard";
        public static final String REDIRECT_TRANSFER = "redirect:/transfer";
        public static final String REDIRECT_TRANSFER_OTP = "redirect:/transfer/otp";
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
        public static final String TRANSFER_REQUEST = "transferRequest";
        public static final String OTP_REQUEST = "otpRequest";
        public static final String RECEIVER_NAME = "receiverName";
        public static final String TRANSACTION_ID = "transactionId";
        public static final String RECURRING_REQUEST = "recurringRequest";
        public static final String SEARCH_RESULTS = "searchResults"; // НОВО
        public static final String QUERY = "query";
        public static final String RECURRING_TRANSACTIONS = "recurringTransactions";
        public static final String WALLET_CREATE_REQUEST = "walletCreateRequest";
        public static final String WALLET_UPDATE_REQUEST = "walletUpdateRequest";
        public static final String ADD_MEMBER_REQUEST = "addMemberRequest";
        public static final String MEMBERS = "members";
        public static final String CURRENT_USER_NAME = "currentUsername";
        public static final String IS_OWNER = "isOwner";
        public static final String WALLET_DTO = "walletDto";
        public static final String CHANGE_EMAIL_REQUEST = "changeEmailDto";
        public static final String BINDING_RESULT_PREFIX = "org.springframework.validation.BindingResult.";
        public static final String CHANGE_PHONE_REQUEST = "changePhoneDto";
        public static final String CHANGE_PASSWORD_REQUEST = "changePasswordDto";
        public static final String TRANSACTIONS_PAGE = "transactionsPage";
        public static final String USERS_PAGE = "usersPage";
        public static final String INFO_MESSAGE = "infoMessage";
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
        public static final String TRANSFER_SUCCESS = "Transfer completed successfully!";
        public static final String OTP_REQUIRED = "Please enter the OTP sent to your device to verify this large transaction.";
        public static final String RECURRING_SETUP_SUCCESS = "Recurring transfer set up successfully!";
        public static final String RECURRING_CANCELLED_SUCCESS = "Recurring transfer has been successfully cancelled.";
        public static final String WALLET_CREATED_SUCCESS = "Wallet created successfully!";
        public static final String WALLET_UPDATED_SUCCESS = "Wallet updated successfully!";
        public static final String WALLET_DELETED_SUCCESS = "Wallet deleted successfully!";
        public static final String MEMBER_ADDED_SUCCESS = "Member added to joint wallet!";
        public static final String MEMBER_REMOVED_SUCCESS = "Member removed from joint wallet!";
        public static final String MEMBERS_MANAGE = "Only the owner can manage members.";
        public static final String SUCCESSFULLY_UPDATED_MEMBER_RIGHTS = "Member rights updated successfully!";
        public static final String UNEXPECTED_ERROR = "An unexpected error occurred.";
        public static final String CONFIRM_DELETE = "Are you sure you want to delete this wallet?";
        public static final String EMAIL_VERIFIED_SUCCESS = "Email verified successfully! You can now login.";
        public static final String EMAIL_UPDATED_SUCCESS = "Email updated successfully!";
        public static final String INVALID_EMAIL_FORMAT = "Please provide a valid email address.";
        public static final String PHONE_UPDATED_SUCCESS = "Phone number updated successfully!";
        public static final String INVALID_PHONE_FORMAT = "Please provide a valid 10-digit phone number.";
        public static final String PASSWORD_UPDATED_SUCCESS = "Password updated successfully!";
        public static final String INVALID_PASSWORD_FORMAT = "Please check your password details and try again.";
        public static final String USER_BLOCKED_SUCCESS = "User has been blocked successfully.";
        public static final String USER_UNBLOCKED_SUCCESS = "User has been unblocked successfully.";
    }

    public static final class Cookies {
        public static final String JWT_COOKIE_NAME = "jwt_token";
        public static final String PATH = "/";
        public static final String SAME_SITE_STRICT = "Strict";
        public static final long MAX_AGE_SECONDS = 3600;
    }
}