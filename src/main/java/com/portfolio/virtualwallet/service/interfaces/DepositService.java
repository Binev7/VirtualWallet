package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.DepositRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;

public interface DepositService {
    TransactionResponseDto depositToWallet(User currentUser, DepositRequestDto request);
}
