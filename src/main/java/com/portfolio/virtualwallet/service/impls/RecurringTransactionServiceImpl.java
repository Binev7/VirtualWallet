package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.RecurringTransaction;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.RecurringTransactionRequestDto;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.repository.RecurringTransactionRepository;
import com.portfolio.virtualwallet.service.interfaces.RecurringTransactionService;
import com.portfolio.virtualwallet.utils.TransactionHelper;
import com.portfolio.virtualwallet.utils.WalletValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecurringTransactionServiceImpl implements RecurringTransactionService {

    private final RecurringTransactionRepository recurringRepository;
    private final WalletValidationHelper validationHelper;
    private final TransactionMapper transactionMapper;
    private final TransactionHelper transactionHelper;

    @Override
    public void createRecurringTransfer(User currentUser, RecurringTransactionRequestDto request) {
        validationHelper.verifyUserCanMakeTransactions(currentUser);
        Wallet senderWallet = validationHelper.getWalletIfOwner(request.getSenderWalletId());
        Wallet receiverWallet = validationHelper.getWalletById(request.getReceiverWalletId());

        LocalDateTime nextExecution = transactionHelper.calculateNextExecutionTime(request.getInterval());

        RecurringTransaction template = transactionMapper.createRecurringTransferEntity(request, senderWallet, receiverWallet, nextExecution);

        recurringRepository.save(template);
    }
}
