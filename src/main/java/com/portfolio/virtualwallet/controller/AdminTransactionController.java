package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.dto.transaction.TransactionAdminDto;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.portfolio.virtualwallet.utils.AppConstants.EntityFields.CREATED_AT;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/v1/admin/transactions")
@RequiredArgsConstructor
public class AdminTransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionAdminDto>> getAllTransactionsForAdmin(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = CREATED_AT) String sortBy) {

        Page<TransactionAdminDto> history = transactionService.getGlobalTransactionsForAdmin(
                startDate, endDate, username, direction, type, status, page, size, sortBy);

        return ResponseEntity.ok(history);
    }
}