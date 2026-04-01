package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.RecurringTransactionRequestDto;

public interface RecurringTransactionService {
    void createRecurringTransfer(User currentUser, RecurringTransactionRequestDto request);

    void cancelRecurringTransfer(User currentUser, Long recurringTransactionId);
}
