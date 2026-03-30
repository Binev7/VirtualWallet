package com.portfolio.virtualwallet.automation.event;

import com.portfolio.virtualwallet.entity.RecurringTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OnRecurringTransactionFailedEvent {
    private final RecurringTransaction recurringTransaction;
    private final String failureReason;
}