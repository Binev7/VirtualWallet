package com.portfolio.virtualwallet.mapper;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.WalletMembership;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletCreateDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletMemberDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletResponseDto;
import com.portfolio.virtualwallet.utils.AppConstants;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletResponseDto toDto(Wallet wallet, WalletMembership membership) {
        return WalletResponseDto.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .balance(wallet.getBalance())
                .isJoint(wallet.isJoint())
                .isDefault(membership.isDefault())
                .isOwner(wallet.getOwner().getId().equals(membership.getUser().getId()))
                .build();
    }

    public WalletMemberDto toMemberDto(WalletMembership membership) {
        return WalletMemberDto.builder()
                .userId(membership.getUser().getId())
                .username(membership.getUser().getUsername())
                .email(membership.getUser().getEmail())
                .canSpend(membership.isCanSpend())
                .canAddMoney(membership.isCanAddMoney())
                .isOwner(membership.getWallet().getOwner().getId().equals(membership.getUser().getId()))
                .build();
    }

    public Wallet createDefaultWalletEntity(User user) {
        return Wallet.builder()
                .name(AppConstants.Wallet.DEFAULT_WALLET_NAME)
                .balance(AppConstants.Wallet.INITIAL_BALANCE)
                .owner(user)
                .isJoint(false)
                .build();
    }

    public Wallet toEntity(WalletCreateDto request, User owner) {
        return Wallet.builder()
                .name(request.getName())
                .balance(AppConstants.Wallet.INITIAL_BALANCE)
                .owner(owner)
                .isJoint(request.isJoint())
                .build();
    }

    public WalletMembership createMembershipEntity(User user, Wallet wallet, boolean canSpend, boolean canAddMoney, boolean isDefault) {
        return WalletMembership.builder()
                .user(user)
                .wallet(wallet)
                .canSpend(canSpend)
                .canAddMoney(canAddMoney)
                .isDefault(isDefault)
                .build();
    }

    public WalletResponseDto toDto(Wallet wallet) {
        return WalletResponseDto.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .balance(wallet.getBalance())
                .isJoint(wallet.isJoint())
                .isDefault(false)
                .build();
    }
}