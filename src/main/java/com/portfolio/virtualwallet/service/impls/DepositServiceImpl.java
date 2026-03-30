package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.DepositRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.integration.bank.BankApiResponse;
import com.portfolio.virtualwallet.integration.bank.BankingGatewayClient;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.repository.TransactionRepository;
import com.portfolio.virtualwallet.service.interfaces.DepositService;
import com.portfolio.virtualwallet.utils.WalletValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.Bank.BANK_REJECTED_TRANSACTION;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.DEPOSIT_COMPLETED;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {

    private final WalletValidationHelper validationHelper;
    private final BankingGatewayClient bankingClient;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionResponseDto depositToWallet(User currentUser, DepositRequestDto request) {
        validationHelper.verifyUserCanMakeTransactions(currentUser);
        Wallet targetWallet = validationHelper.getWalletIfOwner(request.getWalletId());
        Card sourceCard = validationHelper.getCardIfOwner(request.getCardId());

        BankApiResponse bankResponse = bankingClient.processDeposit(sourceCard, request.getCvv(), request.getAmount());

        if (!bankResponse.isSuccess()) {
            throw new IllegalArgumentException(BANK_REJECTED_TRANSACTION);
        }

        Transaction depositTransaction = transactionMapper.createDepositEntity(request.getAmount(), targetWallet, sourceCard);

        targetWallet.setBalance(targetWallet.getBalance().add(request.getAmount()));

        transactionRepository.save(depositTransaction);

        return transactionMapper.toResponseDto(depositTransaction, DEPOSIT_COMPLETED);
    }
}