package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.automation.event.OnLargeTransactionEvent;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.TransactionOtp;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransferRequestDto;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import com.portfolio.virtualwallet.utils.TransactionHelper;
import com.portfolio.virtualwallet.utils.WalletValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.OTP_SENT;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.TRANSFER_COMPLETED;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final WalletValidationHelper validationHelper;
    private final TransactionHelper transactionHelper;
    private final TransactionMapper transactionMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.transaction.large-amount-threshold}")
    private BigDecimal largeAmountThreshold;

    @Override
    @Transactional
    public TransactionResponseDto transfer(User currentUser, TransferRequestDto request) {

        validationHelper.verifyUserCanMakeTransactions(currentUser);
        Wallet senderWallet = validationHelper.getWalletIfOwner(request.getSenderWalletId());
        Wallet receiverWallet = validationHelper.getWalletById(request.getReceiverWalletId());

        validationHelper.verifySufficientFunds(senderWallet, request.getAmount());

        Transaction transaction = transactionMapper.createTransferEntity(request.getAmount(), senderWallet, receiverWallet);

        if (request.getAmount().compareTo(largeAmountThreshold) > 0) {
            TransactionOtp otp = transactionHelper.createAndSaveOtp(transaction);
            eventPublisher.publishEvent(new OnLargeTransactionEvent(otp, currentUser.getEmail()));

            return transactionMapper.toResponseDto(transaction, OTP_SENT);
        } else {
            transactionHelper.executeMoneyTransfer(transaction, senderWallet, receiverWallet);

            return transactionMapper.toResponseDto(transaction, TRANSFER_COMPLETED);
        }
    }
}
