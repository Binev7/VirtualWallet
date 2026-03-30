package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.OtpVerificationRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransferRequestDto;

public interface TransactionService {
    TransactionResponseDto transfer(User currentUser, TransferRequestDto request);
    TransactionResponseDto verifyOtp(User currentUser, OtpVerificationRequestDto request);
}
