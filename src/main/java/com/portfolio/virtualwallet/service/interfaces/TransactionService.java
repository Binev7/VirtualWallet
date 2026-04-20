package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.*;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TransactionService {
    TransactionResponseDto transfer(User currentUser, TransferRequestDto request);

    TransactionResponseDto verifyOtp(User currentUser, OtpVerificationRequestDto request);

    Page<TransactionHistoryDto> getWalletHistory(
            User currentUser, Long walletId, LocalDateTime startDate, LocalDateTime endDate,
            TransactionType type, TransactionStatus status, int page, int size);

    Page<TransactionAdminDto> getGlobalTransactionsForAdmin(
            LocalDateTime startDate, LocalDateTime endDate,
            String username, String direction,
            TransactionType type, TransactionStatus status,
            int page, int size, String sortBy);

    Page<TransactionHistoryDto> getUserTransactions(User currentUser, Pageable pageable);
}

