package com.portfolio.virtualwallet.mapper;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.RecurringTransaction;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.RecurringTransactionRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionHistoryDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.portfolio.virtualwallet.utils.AppConstants.History.*;

@Component
public class TransactionMapper {

    public Transaction createTransferEntity(BigDecimal amount, Wallet sender, Wallet receiver) {
        return Transaction.builder()
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .senderWallet(sender)
                .receiverWallet(receiver)
                .build();
    }

    public TransactionResponseDto toResponseDto(Transaction transaction, String message) {
        return TransactionResponseDto.builder()
                .transactionId(transaction.getId())
                .status(transaction.getStatus())
                .amount(transaction.getAmount())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public RecurringTransaction createRecurringTransferEntity(RecurringTransactionRequestDto request, Wallet sender, Wallet receiver, LocalDateTime nextExecutionTime) {
        return RecurringTransaction.builder()
                .amount(request.getAmount())
                .type(TransactionType.TRANSFER)
                .interval(request.getInterval())
                .senderWallet(sender)
                .receiverWallet(receiver)
                .isActive(true)
                .nextExecutionTime(nextExecutionTime)
                .build();
    }

    public Transaction createDepositEntity(BigDecimal amount, Wallet receiver, Card sourceCard) {
        return Transaction.builder()
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .receiverWallet(receiver)
                .card(sourceCard)
                .status(com.portfolio.virtualwallet.entity.enums.TransactionStatus.COMPLETED)
                .completedAt(LocalDateTime.now())
                .build();
    }

    public Transaction createWithdrawalEntity(BigDecimal amount, Wallet sender, Card targetCard) {
        return Transaction.builder()
                .amount(amount)
                .type(TransactionType.WITHDRAW)
                .senderWallet(sender)
                .card(targetCard)
                .status(com.portfolio.virtualwallet.entity.enums.TransactionStatus.COMPLETED)
                .completedAt(LocalDateTime.now())
                .build();
    }

    public TransactionHistoryDto toHistoryDto(Transaction transaction, Long requestWalletId) {
        boolean isIncoming = false;
        String counterpartyName = EXTERNAL_BANK;

        switch (transaction.getType()) {
            case DEPOSIT -> isIncoming = true;
            case WITHDRAW -> isIncoming = false;
            case TRANSFER -> {
                if (transaction.getReceiverWallet().getId().equals(requestWalletId)) {
                    isIncoming = true;
                    counterpartyName = transaction.getSenderWallet().getOwner().getUsername();
                } else {
                    isIncoming = false;
                    counterpartyName = transaction.getReceiverWallet().getOwner().getUsername();
                }
            }
        }

        return TransactionHistoryDto.builder()
                .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType().name())
                .status(transaction.getStatus().name())
                .date(transaction.getCreatedAt())
                .counterparty(counterpartyName)
                .direction(isIncoming ? INCOMING_DIRECTION : OUTGOING_DIRECTION)
                .build();
    }
}