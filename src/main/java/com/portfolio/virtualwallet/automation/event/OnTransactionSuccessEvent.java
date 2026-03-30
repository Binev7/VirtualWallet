package com.portfolio.virtualwallet.automation.event;

import com.portfolio.virtualwallet.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OnTransactionSuccessEvent {
    private final Transaction transaction;
}