package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.dto.wallet.AddWalletMemberDto;
import com.portfolio.virtualwallet.entity.dto.wallet.UpdateWalletMemberRightsDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletMemberDto;

import java.util.List;

public interface JointWalletService {

    List<WalletMemberDto> getWalletMembers(Long walletId);

    void addMemberToJointWallet(Long walletId, AddWalletMemberDto request);

    void removeMemberFromJointWallet(Long walletId, Long targetUserId);

    void updateMemberRights(Long walletId, Long targetUserId, UpdateWalletMemberRightsDto request);
}
