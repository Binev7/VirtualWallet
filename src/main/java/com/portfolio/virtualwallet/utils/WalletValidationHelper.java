package com.portfolio.virtualwallet.utils;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.ExceptionMessages;
import com.portfolio.virtualwallet.exception.UnauthorizedException;
import com.portfolio.virtualwallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.Wallet.*;

@Component
@RequiredArgsConstructor
public class WalletValidationHelper {

    private final WalletRepository walletRepository;

    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException(WALLET_NOT_FOUND));
    }

    public Wallet getWalletIfOwner(Long walletId) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException(WALLET_NOT_FOUND));

        if (!wallet.getOwner().getUsername().equals(currentUsername)) {
            throw new UnauthorizedException(WALLET_NOT_OWNER);
        }

        return wallet;
    }

    public void verifyUserCanMakeTransactions(User user) {
        if (!user.isEmailVerified()) {
            throw new UnauthorizedException(ExceptionMessages.Verification.UNVERIFIED_EMAIL_ACTION);
        }
    }

    public void verifySufficientFunds(Wallet wallet, BigDecimal amount) {
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(ExceptionMessages.Transaction.INSUFFICIENT_FUNDS);
        }
    }
}