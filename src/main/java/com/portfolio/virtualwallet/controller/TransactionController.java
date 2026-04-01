package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.*;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import com.portfolio.virtualwallet.service.interfaces.RecurringTransactionService;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.RECURRING_CANCELLED_SUCCESS;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.RECURRING_CREATED_SUCCESS;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final RecurringTransactionService recurringTransactionService;


    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transferMoney(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody TransferRequestDto request) {

        TransactionResponseDto response = transactionService.transfer(currentUser, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<TransactionResponseDto> verifyLargeTransaction(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody OtpVerificationRequestDto request) {

        TransactionResponseDto response = transactionService.verifyOtp(currentUser, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<TransactionHistoryDto>> getTransactionHistory(
            @AuthenticationPrincipal User currentUser,
            @RequestParam Long walletId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {

        Page<TransactionHistoryDto> history = transactionService.getWalletHistory(
                currentUser, walletId, startDate, endDate, type, status, page, size);

        return ResponseEntity.ok(history);
    }

    @PostMapping("/recurring")
    public ResponseEntity<Map<String, String>> createRecurringTransaction(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody RecurringTransactionRequestDto request) {

        recurringTransactionService.createRecurringTransfer(currentUser, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", RECURRING_CREATED_SUCCESS));
    }

    @DeleteMapping("/recurring/{id}")
    public ResponseEntity<Map<String, String>> cancelRecurringTransaction(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {

        recurringTransactionService.cancelRecurringTransfer(currentUser, id);

        return ResponseEntity.ok(Map.of("message", RECURRING_CANCELLED_SUCCESS));
    }
}


