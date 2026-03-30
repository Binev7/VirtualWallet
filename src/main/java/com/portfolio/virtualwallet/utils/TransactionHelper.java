package com.portfolio.virtualwallet.utils;

import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.TransactionOtp;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.repository.TransactionOtpRepository;
import com.portfolio.virtualwallet.repository.TransactionRepository;
import com.portfolio.virtualwallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransactionHelper {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionOtpRepository otpRepository;

    public void executeMoneyTransfer(Transaction transaction, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(transaction.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transaction.getAmount()));

        walletRepository.save(sender);
        walletRepository.save(receiver);

        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public TransactionOtp createAndSaveOtp(Transaction transaction) {
        transaction.setStatus(TransactionStatus.PENDING_VERIFICATION);
        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionOtp otp = TransactionOtp.builder()
                .otpCode(generateOtpCode())
                .transaction(savedTransaction)
                .expirationTime(LocalDateTime.now().plusMinutes(TransactionOtp.EXPIRATION_MINUTES))
                .build();

        return otpRepository.save(otp);
    }

    private String generateOtpCode() {
        SecureRandom secureRandom = new SecureRandom();
        int randomCode = secureRandom.nextInt(1000000);
        return String.format("%06d", randomCode);
    }
}