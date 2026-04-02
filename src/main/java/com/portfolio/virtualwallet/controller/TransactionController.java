package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.*;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import com.portfolio.virtualwallet.service.interfaces.RecurringTransactionService;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static com.portfolio.virtualwallet.controller.docs.SwaggerMessages.Transaction.*;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.RECURRING_CANCELLED_SUCCESS;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.RECURRING_CREATED_SUCCESS;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
public class TransactionController {

    private final TransactionService transactionService;
    private final RecurringTransactionService recurringTransactionService;

    @Operation(summary = TRANSFER_SUMMARY, description = TRANSFER_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED_401, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transferMoney(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody TransferRequestDto request) {

        TransactionResponseDto response = transactionService.transfer(currentUser, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = VERIFY_OTP_SUMMARY, description = VERIFY_OTP_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content)
    })
    @PostMapping("/verify-otp")
    public ResponseEntity<TransactionResponseDto> verifyLargeTransaction(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody OtpVerificationRequestDto request) {

        TransactionResponseDto response = transactionService.verifyOtp(currentUser, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = HISTORY_SUMMARY, description = HISTORY_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
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

    @Operation(summary = RECURRING_SUMMARY, description = RECURRING_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = CREATED_201),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PostMapping("/recurring")
    public ResponseEntity<Map<String, String>> createRecurringTransaction(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody RecurringTransactionRequestDto request) {

        recurringTransactionService.createRecurringTransfer(currentUser, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", RECURRING_CREATED_SUCCESS));
    }

    @Operation(summary = CANCEL_RECURRING_SUMMARY, description = CANCEL_RECURRING_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @DeleteMapping("/recurring/{id}")
    public ResponseEntity<Map<String, String>> cancelRecurringTransaction(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id) {

        recurringTransactionService.cancelRecurringTransfer(currentUser, id);

        return ResponseEntity.ok(Map.of("message", RECURRING_CANCELLED_SUCCESS));
    }
}