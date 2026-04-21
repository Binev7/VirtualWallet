package com.portfolio.virtualwallet.exception;

public class AdminProtectionException extends RuntimeException {

    public AdminProtectionException(String message) {
        super(message);
    }

    public AdminProtectionException(String message, Throwable cause) {
        super(message, cause);
    }
}