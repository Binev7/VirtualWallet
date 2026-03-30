package com.portfolio.virtualwallet.automation.scheduler;

import com.portfolio.virtualwallet.automation.event.OnRecurringTransactionFailedEvent;
import com.portfolio.virtualwallet.automation.event.OnTransactionSuccessEvent;
import com.portfolio.virtualwallet.entity.RecurringTransaction;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.exception.ExceptionMessages;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.repository.RecurringTransactionRepository;
import com.portfolio.virtualwallet.utils.TransactionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecurringTransactionScheduler {

    private final RecurringTransactionRepository recurringRepository;
    private final TransactionHelper transactionHelper;
    private final TransactionMapper transactionMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "${app.cron.recurring-transactions}")
    @Transactional
    public void processDueTransactions() {
        LocalDateTime now = LocalDateTime.now();
        List<RecurringTransaction> dueTransactions = recurringRepository.findAllDueTransactions(now);

        if (dueTransactions.isEmpty()) {
            return;
        }

        log.info("Found {} due recurring transactions to process.", dueTransactions.size());

        for (RecurringTransaction recurringTx : dueTransactions) {
            processSingleRecurringTransaction(recurringTx);
        }
    }

    private void processSingleRecurringTransaction(RecurringTransaction recurringTx) {
        Wallet sender = recurringTx.getSenderWallet();
        Wallet receiver = recurringTx.getReceiverWallet();
        BigDecimal amount = recurringTx.getAmount();

        if (sender.getBalance().compareTo(amount) < 0) {
            recurringTx.setActive(false);
            recurringRepository.save(recurringTx);

            eventPublisher.publishEvent(new OnRecurringTransactionFailedEvent(recurringTx, ExceptionMessages.Transaction.INSUFFICIENT_FUNDS));
            return;
        }

        Transaction transaction = transactionMapper.createTransferEntity(amount, sender, receiver);
        transactionHelper.executeMoneyTransfer(transaction, sender, receiver);

        LocalDateTime nextDate = transactionHelper.calculateNextExecutionTime(recurringTx.getInterval());
        recurringTx.setNextExecutionTime(nextDate);
        recurringRepository.save(recurringTx);

        eventPublisher.publishEvent(new OnTransactionSuccessEvent(transaction));
    }
}