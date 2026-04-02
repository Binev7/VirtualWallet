package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.WithdrawalRequestDto;
import com.portfolio.virtualwallet.integration.bank.BankApiResponse;
import com.portfolio.virtualwallet.integration.bank.BankingGatewayClient;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.repository.TransactionRepository;
import com.portfolio.virtualwallet.service.interfaces.WithdrawalService;
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.Bank.BANK_REJECTED_TRANSACTION;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.WITHDRAWAL_COMPLETED;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {

    private final TransactionValidationHelper validationHelper;
    private final BankingGatewayClient bankingClient;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionResponseDto withdrawFromWallet(User currentUser, WithdrawalRequestDto request) {

        validationHelper.verifyUserCanMakeTransactions(currentUser);
        Wallet sourceWallet = validationHelper.getWalletIfOwner(request.getWalletId());
        Card targetCard = validationHelper.getCardIfOwner(request.getCardId());

        validationHelper.verifySufficientFunds(sourceWallet, request.getAmount());

        BankApiResponse bankResponse = bankingClient.processWithdrawal(targetCard, request.getAmount());

        if (!bankResponse.isSuccess()) {
            throw new IllegalArgumentException(BANK_REJECTED_TRANSACTION);
        }

        Transaction withdrawTransaction = transactionMapper.createWithdrawalEntity(request.getAmount(), sourceWallet, targetCard);

        sourceWallet.setBalance(sourceWallet.getBalance().subtract(request.getAmount()));

        transactionRepository.save(withdrawTransaction);

        return transactionMapper.toResponseDto(withdrawTransaction, WITHDRAWAL_COMPLETED);
    }
}