package com.portfolio.virtualwallet.service.interfaces;

public interface EmailService {
    void sendVerificationEmail(String to, String confirmationUrl);
}