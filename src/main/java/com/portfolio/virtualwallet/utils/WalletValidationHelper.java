package com.portfolio.virtualwallet.utils;

import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.UnauthorizedException;
import com.portfolio.virtualwallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.Wallet.*;

@Component
@RequiredArgsConstructor
public class WalletValidationHelper {

    private final WalletRepository walletRepository;

    public Wallet getWalletIfOwner(Long walletId) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException(WALLET_NOT_FOUND));

        if (!wallet.getOwner().getUsername().equals(currentUsername)) {
            throw new UnauthorizedException(WALLET_NOT_OWNER);
        }

        return wallet;
    }
}