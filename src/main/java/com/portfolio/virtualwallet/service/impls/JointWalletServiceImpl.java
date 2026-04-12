package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.WalletMembership;
import com.portfolio.virtualwallet.entity.dto.wallet.AddWalletMemberDto;
import com.portfolio.virtualwallet.entity.dto.wallet.UpdateWalletMemberRightsDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletMemberDto;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.UnauthorizedException;
import com.portfolio.virtualwallet.mapper.WalletMapper;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.WalletMembershipRepository;
import com.portfolio.virtualwallet.service.interfaces.JointWalletService;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.User.USER_NOT_FOUND;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.Wallet.*;
import static com.portfolio.virtualwallet.utils.AppConstants.Wallet.UNAUTHORIZED_MEMBER;

@Service
@RequiredArgsConstructor
public class JointWalletServiceImpl implements JointWalletService {

    private final WalletMembershipRepository walletMembershipRepository;
    private final UserRepository userRepository;
    private final WalletMapper walletMapper;
    private final TransactionValidationHelper transactionValidationHelper;


    @Override
    @Transactional(readOnly = true)
    public List<WalletMemberDto> getWalletMembers(Long walletId) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        walletMembershipRepository.findByWalletIdAndUserUsername(walletId, currentUsername)
                .orElseThrow(() -> new UnauthorizedException(UNAUTHORIZED_MEMBER));

        return walletMembershipRepository.findAllByWalletId(walletId)
                .stream()
                .map(walletMapper::toMemberDto)
                .toList();
    }

    @Override
    @Transactional
    public void addMemberToJointWallet(Long walletId, AddWalletMemberDto request) {
        Wallet wallet = transactionValidationHelper.getWalletIfOwner(walletId);

        if (!wallet.isJoint()) {
            throw new UnauthorizedException(WALLET_NOT_JOINT);
        }

        User targetUser = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        if (walletMembershipRepository.findByWalletIdAndUserId(walletId, targetUser.getId()).isPresent()) {
            throw new DuplicateEntityException(WALLET_USER_ALREADY_MEMBER);
        }

        WalletMembership newMember = walletMapper.createMembershipEntity(
                targetUser, wallet, request.isCanSpend(), request.isCanAddMoney(), false
        );

        walletMembershipRepository.save(newMember);
    }

    @Override
    @Transactional
    public void removeMemberFromJointWallet(Long walletId, Long targetUserId) {
        Wallet wallet = transactionValidationHelper.getWalletIfOwner(walletId);

        if (wallet.getOwner().getId().equals(targetUserId)) {
            throw new UnauthorizedException(WALLET_CANNOT_REMOVE_OWNER);
        }

        WalletMembership membershipToRemove = walletMembershipRepository
                .findByWalletIdAndUserId(walletId, targetUserId)
                .orElseThrow(() -> new EntityNotFoundException(WALLET_NOT_FOUND));

        walletMembershipRepository.delete(membershipToRemove);
    }

    @Override
    @Transactional
    public void updateMemberRights(Long walletId, Long targetUserId, UpdateWalletMemberRightsDto request) {
        Wallet wallet = transactionValidationHelper.getWalletIfOwner(walletId);

        if (wallet.getOwner().getId().equals(targetUserId)) {
            throw new UnauthorizedException(WALLET_CANNOT_MODIFY_OWNER_RIGHTS);
        }

        WalletMembership membershipToUpdate = walletMembershipRepository
                .findByWalletIdAndUserId(walletId, targetUserId)
                .orElseThrow(() -> new EntityNotFoundException(WALLET_NOT_FOUND));

        membershipToUpdate.setCanSpend(request.isCanSpend());
        membershipToUpdate.setCanAddMoney(request.isCanAddMoney());

        walletMembershipRepository.save(membershipToUpdate);
    }
}