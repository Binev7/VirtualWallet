package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.WithdrawalRequestDto;

public interface WithdrawalService {
    TransactionResponseDto withdrawFromWallet(User currentUser, WithdrawalRequestDto request);
}
