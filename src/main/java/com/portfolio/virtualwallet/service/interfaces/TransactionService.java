package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.OtpVerificationRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionHistoryDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransferRequestDto;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface TransactionService {
    TransactionResponseDto transfer(User currentUser, TransferRequestDto request);
    TransactionResponseDto verifyOtp(User currentUser, OtpVerificationRequestDto request);
    Page<TransactionHistoryDto> getWalletHistory(
            User currentUser, Long walletId, LocalDateTime startDate, LocalDateTime endDate,
            TransactionType type, TransactionStatus status, int page, int size);
}
