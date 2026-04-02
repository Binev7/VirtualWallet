package com.portfolio.virtualwallet.controller.docs;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SwaggerMessages {

    @UtilityClass
    public static class AdminTransaction {
        public static final String TAG_NAME = "Admin Transactions API";
        public static final String TAG_DESCRIPTION = "Endpoints for administrators to monitor and manage platform transactions.";

        public static final String GET_ALL_SUMMARY = "Get all global transactions";
        public static final String GET_ALL_DESCRIPTION = "Retrieves a paginated list of all transactions across the platform. Supports advanced filtering by date range, username, transaction direction, type, and status.";

        public static final String SUCCESS_200 = "Successfully retrieved the paginated list of transactions.";
        public static final String FORBIDDEN_403 = "Forbidden - Admin access required.";
    }

    @UtilityClass
    public static class AdminUser {
        public static final String TAG_NAME = "Admin Users API";
        public static final String TAG_DESCRIPTION = "Endpoints for administrators to manage user accounts and statuses.";

        public static final String GET_ALL_SUMMARY = "Search and list users";
        public static final String GET_ALL_DESCRIPTION = "Retrieves a paginated list of all users with detailed admin view. Supports searching by username, email, or phone.";

        public static final String TOGGLE_BLOCK_SUMMARY = "Toggle user block status";
        public static final String TOGGLE_BLOCK_DESCRIPTION = "Blocks or unblocks a specific user account by their ID. Blocked users cannot perform transactions.";

        public static final String SUCCESS_200 = "Successfully retrieved data/performed action.";
        public static final String SUCCESS_204 = "User block status updated successfully.";
        public static final String BAD_REQUEST_400 = "Invalid request parameters.";
        public static final String FORBIDDEN_403 = "Forbidden - Admin access required.";
        public static final String NOT_FOUND_404 = "User not found.";
    }

    @UtilityClass
    public static class Auth {
        public static final String TAG_NAME = "Authentication API";
        public static final String TAG_DESCRIPTION = "Endpoints for user registration, login, email verification, and password recovery.";

        public static final String REGISTER_SUMMARY = "Register a new user";
        public static final String REGISTER_DESCRIPTION = "Creates a new user account and sends an email verification link to the provided email address.";

        public static final String LOGIN_SUMMARY = "User login";
        public static final String LOGIN_DESCRIPTION = "Authenticates a user with email/username and password. Returns a JWT token upon successful authentication.";

        public static final String VERIFY_EMAIL_SUMMARY = "Verify user email";
        public static final String VERIFY_EMAIL_DESCRIPTION = "Verifies a user's email address using the token sent to their inbox after registration or email change.";

        public static final String FORGOT_PASSWORD_SUMMARY = "Forgot password request";
        public static final String FORGOT_PASSWORD_DESCRIPTION = "Initiates the password reset process by generating a token and sending a reset link to the user's email.";

        public static final String RESET_PASSWORD_SUMMARY = "Reset password";
        public static final String RESET_PASSWORD_DESCRIPTION = "Resets the user's password using the token provided via email.";

        public static final String SUCCESS_200 = "Operation completed successfully.";
        public static final String CREATED_201 = "User registered successfully.";
        public static final String BAD_REQUEST_400 = "Invalid input data or validation error.";
        public static final String UNAUTHORIZED_401 = "Invalid credentials or unverified email.";
        public static final String NOT_FOUND_404 = "User or token not found.";
        public static final String CONFLICT_409 = "Username, email, or phone already exists.";
    }

    @UtilityClass
    public static class Card {
        public static final String TAG_NAME = "Cards API";
        public static final String TAG_DESCRIPTION = "Endpoints for managing user debit and credit cards.";

        public static final String ADD_CARD_SUMMARY = "Add a new card";
        public static final String ADD_CARD_DESCRIPTION = "Links a new credit or debit card to the authenticated user's profile.";

        public static final String GET_ALL_SUMMARY = "Get all my cards";
        public static final String GET_ALL_DESCRIPTION = "Retrieves a list of all cards belonging to the currently authenticated user.";

        public static final String GET_BY_ID_SUMMARY = "Get card by ID";
        public static final String GET_BY_ID_DESCRIPTION = "Retrieves details of a specific card by its ID. Users can only access their own cards.";

        public static final String UPDATE_CARD_SUMMARY = "Update a card";
        public static final String UPDATE_CARD_DESCRIPTION = "Updates the details of an existing card (e.g., expiration date, card holder name).";

        public static final String DELETE_CARD_SUMMARY = "Delete a card";
        public static final String DELETE_CARD_DESCRIPTION = "Removes a linked card from the user's profile.";

        public static final String SUCCESS_200 = "Operation completed successfully.";
        public static final String CREATED_201 = "Card successfully added.";
        public static final String SUCCESS_204 = "Card successfully deleted.";
        public static final String BAD_REQUEST_400 = "Invalid request parameters.";
        public static final String FORBIDDEN_403 = "Forbidden - Not the owner of the card.";
        public static final String NOT_FOUND_404 = "Card not found.";
    }

    @UtilityClass
    public static class Transaction {
        public static final String TAG_NAME = "Transactions API";
        public static final String TAG_DESCRIPTION = "Endpoints for managing money transfers, OTP verification, and transaction history.";

        public static final String TRANSFER_SUMMARY = "Transfer money between wallets";
        public static final String TRANSFER_DESCRIPTION = "Initiates a money transfer. If the amount exceeds the large transaction threshold, the transfer is set to PENDING and an OTP is sent to the user's email.";

        public static final String VERIFY_OTP_SUMMARY = "Verify OTP for large transactions";
        public static final String VERIFY_OTP_DESCRIPTION = "Verifies the 6-digit OTP code sent to the user's email to complete a pending large transaction.";

        public static final String HISTORY_SUMMARY = "Get transaction history";
        public static final String HISTORY_DESCRIPTION = "Retrieves a paginated history of transactions for a specific wallet. Supports filtering by date range, transaction type, and status.";

        public static final String RECURRING_SUMMARY = "Schedule a recurring transaction";
        public static final String RECURRING_DESCRIPTION = "Creates a new recurring transaction (subscription) that will be executed automatically based on the specified interval.";

        public static final String CANCEL_RECURRING_SUMMARY = "Cancel a recurring transaction";
        public static final String CANCEL_RECURRING_DESCRIPTION = "Deletes an active recurring transaction. Only the owner of the sender wallet can perform this action.";

        public static final String SUCCESS_200 = "Operation completed successfully.";
        public static final String CREATED_201 = "Recurring transaction successfully scheduled.";
        public static final String BAD_REQUEST_400 = "Invalid request parameters or insufficient funds.";
        public static final String UNAUTHORIZED_401 = "Unauthorized or blocked user.";
        public static final String FORBIDDEN_403 = "Forbidden - Not the owner of the wallet or transaction.";
        public static final String NOT_FOUND_404 = "Wallet or transaction not found.";
    }

    @UtilityClass
    public static class User {
        public static final String TAG_NAME = "Users API";
        public static final String TAG_DESCRIPTION = "Endpoints for managing user profiles and searching public user data.";

        public static final String SEARCH_SUMMARY = "Search public users";
        public static final String SEARCH_DESCRIPTION = "Searches for users by username, email, or phone. Returns only public, non-sensitive data, intended for scenarios like finding a user to send money to.";

        public static final String CHANGE_EMAIL_SUMMARY = "Change user email";
        public static final String CHANGE_EMAIL_DESCRIPTION = "Allows the authenticated user to change their email address. Triggers a new email verification process.";

        public static final String SUCCESS_200 = "Operation completed successfully.";
        public static final String BAD_REQUEST_400 = "Invalid request parameters.";
        public static final String CONFLICT_409 = "The new email address is already in use.";
    }

    @UtilityClass
    public static class Wallet {
        public static final String TAG_NAME = "Wallets API";
        public static final String TAG_DESCRIPTION = "Endpoints for managing personal and joint wallets, including member permissions.";

        public static final String GET_MY_WALLETS_SUMMARY = "Get my wallets";
        public static final String GET_MY_WALLETS_DESCRIPTION = "Retrieves a list of all wallets owned by the authenticated user, as well as joint wallets they are a member of.";

        public static final String GET_MEMBERS_SUMMARY = "Get wallet members";
        public static final String GET_MEMBERS_DESCRIPTION = "Retrieves a list of all members (and their permissions) for a specific joint wallet.";

        public static final String CREATE_WALLET_SUMMARY = "Create a new wallet";
        public static final String CREATE_WALLET_DESCRIPTION = "Creates a new personal or joint wallet for the authenticated user.";

        public static final String UPDATE_WALLET_SUMMARY = "Update a wallet";
        public static final String UPDATE_WALLET_DESCRIPTION = "Updates the name or settings of an existing wallet. Only the owner can perform this action.";

        public static final String DELETE_WALLET_SUMMARY = "Delete a wallet";
        public static final String DELETE_WALLET_DESCRIPTION = "Deletes a wallet. The wallet must have a zero balance to be deleted.";

        public static final String ADD_MEMBER_SUMMARY = "Add a member to a joint wallet";
        public static final String ADD_MEMBER_DESCRIPTION = "Adds a new user to an existing joint wallet with specific transaction permissions.";

        public static final String UPDATE_RIGHTS_SUMMARY = "Update member permissions";
        public static final String UPDATE_RIGHTS_DESCRIPTION = "Modifies the transaction rights (deposit, withdraw, etc.) of an existing member in a joint wallet.";

        public static final String REMOVE_MEMBER_SUMMARY = "Remove a member";
        public static final String REMOVE_MEMBER_DESCRIPTION = "Removes a member from a joint wallet. The owner cannot be removed.";

        public static final String SUCCESS_200 = "Operation completed successfully.";
        public static final String CREATED_201 = "Resource successfully created.";
        public static final String SUCCESS_204 = "Resource successfully deleted/removed.";
        public static final String BAD_REQUEST_400 = "Invalid parameters or business rule violation (e.g., wallet not empty).";
        public static final String FORBIDDEN_403 = "Forbidden - Not the owner of the wallet.";
        public static final String NOT_FOUND_404 = "Wallet or user not found.";
    }

    @UtilityClass
    public static class Funding {
        public static final String TAG_NAME = "Wallet Funding API";
        public static final String TAG_DESCRIPTION = "Endpoints for depositing money into wallets from cards and withdrawing money back to cards.";

        public static final String DEPOSIT_SUMMARY = "Deposit money to wallet";
        public static final String DEPOSIT_DESCRIPTION = "Transfers a specified amount from a linked card to a personal or joint wallet. Requires valid card and wallet IDs.";

        public static final String WITHDRAW_SUMMARY = "Withdraw money from wallet";
        public static final String WITHDRAW_DESCRIPTION = "Transfers a specified amount from a wallet back to a linked card. Ensures sufficient balance before processing.";

        public static final String SUCCESS_200 = "Transaction completed successfully.";
        public static final String BAD_REQUEST_400 = "Invalid transaction parameters, insufficient funds, or card/wallet mismatch.";
        public static final String FORBIDDEN_403 = "Forbidden - User does not have rights to use this card or wallet.";
        public static final String NOT_FOUND_404 = "Card or wallet not found.";
    }
}