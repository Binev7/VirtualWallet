package com.portfolio.virtualwallet.exception;

import org.springframework.security.core.AuthenticationException;

public class UserBlockedException extends AuthenticationException {

    public UserBlockedException(String msg) {
        super(msg);
    }

    public UserBlockedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}