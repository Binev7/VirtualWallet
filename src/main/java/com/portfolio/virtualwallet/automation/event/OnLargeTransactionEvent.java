package com.portfolio.virtualwallet.automation.event;

import com.portfolio.virtualwallet.entity.TransactionOtp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OnLargeTransactionEvent {
    private final TransactionOtp otp;
    private final String userEmail;
}