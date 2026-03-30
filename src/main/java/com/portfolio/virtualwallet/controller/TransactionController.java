package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.OtpVerificationRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransferRequestDto;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

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
}


