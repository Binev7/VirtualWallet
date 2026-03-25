package com.portfolio.virtualwallet.exception;

public class WalletNotEmptyException extends RuntimeException {

    public WalletNotEmptyException(String message) {
        super(message);
    }
}