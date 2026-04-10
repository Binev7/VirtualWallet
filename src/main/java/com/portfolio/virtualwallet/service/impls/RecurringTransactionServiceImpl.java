package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.RecurringTransaction;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.RecurringTransactionRequestDto;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.UnauthorizedException;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.repository.RecurringTransactionRepository;
import com.portfolio.virtualwallet.service.interfaces.RecurringTransactionService;
import com.portfolio.virtualwallet.utils.TransactionHelper;
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.Transaction.NOT_OWNER_OF_RECURRING;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.Transaction.RECURRING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RecurringTransactionServiceImpl implements RecurringTransactionService {

    private final RecurringTransactionRepository recurringRepository;
    private final TransactionValidationHelper validationHelper;
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

    @Override
    @Transactional
    public void cancelRecurringTransfer(User currentUser, Long recurringTransactionId) {
        RecurringTransaction recurringTx = recurringRepository.findById(recurringTransactionId)
                .orElseThrow(() -> new EntityNotFoundException(RECURRING_NOT_FOUND));

        if (!recurringTx.getSenderWallet().getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException(NOT_OWNER_OF_RECURRING);
        }

        recurringRepository.delete(recurringTx);
    }

    @Override
    public List<RecurringTransaction> getUserRecurringTransfers(User currentUser) {
        return recurringRepository.findAllActiveByUser(currentUser);
    }
}
