package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.wallet.*;

import java.util.List;

public interface WalletService {

    List<WalletResponseDto> getMyWallets();

    void initializeDefaultWallet(User user);

    WalletResponseDto createWallet(WalletCreateDto request);

    WalletResponseDto updateWallet(Long id, WalletUpdateDto request);

    void deleteWallet(Long id);

    List<WalletResponseDto> getUserWallets(User user);
}
