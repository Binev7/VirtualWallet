package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.DepositRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.WithdrawalRequestDto;
import com.portfolio.virtualwallet.service.interfaces.DepositService;
import com.portfolio.virtualwallet.service.interfaces.WithdrawalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/funding")
@RequiredArgsConstructor
public class WalletFundingController {

    private final DepositService depositService;
    private final WithdrawalService withdrawalService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody DepositRequestDto request) {

        TransactionResponseDto response = depositService.depositToWallet(currentUser, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody WithdrawalRequestDto request) {

        TransactionResponseDto response = withdrawalService.withdrawFromWallet(currentUser, request);
        return ResponseEntity.ok(response);
    }
}