package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.DepositRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.WithdrawalRequestDto;
import com.portfolio.virtualwallet.service.interfaces.DepositService;
import com.portfolio.virtualwallet.service.interfaces.WithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.portfolio.virtualwallet.controller.docs.SwaggerMessages.Funding.*;

@RestController
@RequestMapping("/api/v1/funding")
@RequiredArgsConstructor
@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
public class WalletFundingController {

    private final DepositService depositService;
    private final WithdrawalService withdrawalService;

    @Operation(summary = DEPOSIT_SUMMARY, description = DEPOSIT_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDto> deposit(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody DepositRequestDto request) {

        TransactionResponseDto response = depositService.depositToWallet(currentUser, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = WITHDRAW_SUMMARY, description = WITHDRAW_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody WithdrawalRequestDto request) {

        TransactionResponseDto response = withdrawalService.withdrawFromWallet(currentUser, request);
        return ResponseEntity.ok(response);
    }
}