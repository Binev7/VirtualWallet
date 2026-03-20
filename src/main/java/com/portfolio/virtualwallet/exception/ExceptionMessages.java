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
    }

    @UtilityClass
    public class Card {
        public final String ALREADY_EXISTS = "Card with this number already exists.";
        public final String NOT_FOUND = "Card not found.";
        public final String INVALID_OWNER = "You are not the owner of this card.";
    }

}