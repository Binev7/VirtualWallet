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
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JointWalletServiceImplTest {

    @Mock
    private WalletMembershipRepository walletMembershipRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletMapper walletMapper;
    @Mock
    private TransactionValidationHelper transactionValidationHelper;

    @InjectMocks
    private JointWalletServiceImpl jointWalletService;

    private Wallet jointWallet;
    private User owner;
    private User targetUser;
    private WalletMembership membership;

    @BeforeEach
    void setUp() {
        owner = User.builder().id(1L).email("owner@test.com").build();
        targetUser = User.builder().id(2L).email("target@test.com").build();
        jointWallet = Wallet.builder().id(1L).isJoint(true).owner(owner).build();
        membership = WalletMembership.builder().id(1L).wallet(jointWallet).user(targetUser).build();
    }

    @Test
    void getWalletMembers_ShouldReturnList() {
        when(walletMembershipRepository.findAllByWalletId(1L)).thenReturn(List.of(membership));
        when(walletMapper.toMemberDto(any())).thenReturn(new WalletMemberDto());

        List<WalletMemberDto> result = jointWalletService.getWalletMembers(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionValidationHelper).getWalletIfOwner(1L);
    }

    @Test
    void addMemberToJointWallet_Success() {
        AddWalletMemberDto request = new AddWalletMemberDto("target@test.com", true, true);

        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(jointWallet);
        when(userRepository.findByEmail("target@test.com")).thenReturn(Optional.of(targetUser));
        when(walletMembershipRepository.findByWalletIdAndUserId(1L, 2L)).thenReturn(Optional.empty());
        when(walletMapper.createMembershipEntity(any(), any(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(membership);

        jointWalletService.addMemberToJointWallet(1L, request);

        verify(walletMembershipRepository).save(any());
    }

    @Test
    void addMemberToJointWallet_ThrowsUnauthorized_WhenNotJoint() {
        jointWallet.setJoint(false);
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(jointWallet);

        assertThrows(UnauthorizedException.class, () ->
                jointWalletService.addMemberToJointWallet(1L, new AddWalletMemberDto()));
    }

    @Test
    void addMemberToJointWallet_ThrowsDuplicate_WhenAlreadyMember() {
        AddWalletMemberDto request = new AddWalletMemberDto("target@test.com", true, true);
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(jointWallet);
        when(userRepository.findByEmail("target@test.com")).thenReturn(Optional.of(targetUser));
        when(walletMembershipRepository.findByWalletIdAndUserId(1L, 2L)).thenReturn(Optional.of(membership));

        assertThrows(DuplicateEntityException.class, () -> jointWalletService.addMemberToJointWallet(1L, request));
    }

    @Test
    void removeMemberFromJointWallet_Success() {
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(jointWallet);
        when(walletMembershipRepository.findByWalletIdAndUserId(1L, 2L)).thenReturn(Optional.of(membership));

        jointWalletService.removeMemberFromJointWallet(1L, 2L);

        verify(walletMembershipRepository).delete(membership);
    }

    @Test
    void removeMemberFromJointWallet_ThrowsUnauthorized_WhenRemovingOwner() {
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(jointWallet);

        assertThrows(UnauthorizedException.class, () -> jointWalletService.removeMemberFromJointWallet(1L, 1L));
    }

    @Test
    void updateMemberRights_Success() {
        UpdateWalletMemberRightsDto request = new UpdateWalletMemberRightsDto(false, false);
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(jointWallet);
        when(walletMembershipRepository.findByWalletIdAndUserId(1L, 2L)).thenReturn(Optional.of(membership));

        jointWalletService.updateMemberRights(1L, 2L, request);

        assertFalse(membership.isCanSpend());
        assertFalse(membership.isCanAddMoney());
        verify(walletMembershipRepository).save(membership);
    }

    @Test
    void updateMemberRights_ThrowsUnauthorized_WhenModifyingOwner() {
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(jointWallet);

        assertThrows(UnauthorizedException.class, () ->
                jointWalletService.updateMemberRights(1L, 1L, new UpdateWalletMemberRightsDto()));
    }

    @Test
    void updateMemberRights_ThrowsNotFound_WhenMembershipMissing() {
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(jointWallet);
        when(walletMembershipRepository.findByWalletIdAndUserId(1L, 2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                jointWalletService.updateMemberRights(1L, 2L, new UpdateWalletMemberRightsDto()));
    }
}