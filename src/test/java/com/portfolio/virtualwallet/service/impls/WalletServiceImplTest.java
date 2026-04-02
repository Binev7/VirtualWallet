package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.WalletMembership;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletCreateDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletResponseDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletUpdateDto;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.exception.WalletNotEmptyException;
import com.portfolio.virtualwallet.mapper.WalletMapper;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.WalletMembershipRepository;
import com.portfolio.virtualwallet.repository.WalletRepository;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock private WalletRepository walletRepository;
    @Mock private WalletMembershipRepository walletMembershipRepository;
    @Mock private UserRepository userRepository;
    @Mock private WalletMapper walletMapper;
    @Mock private TransactionValidationHelper transactionValidationHelper;

    @InjectMocks
    private WalletServiceImpl walletService;

    private MockedStatic<SecurityUtils> mockedSecurityUtils;
    private User testUser;
    private Wallet testWallet;
    private WalletMembership testMembership;
    private final String USERNAME = "testUser";

    @BeforeEach
    void setUp() {
        mockedSecurityUtils = mockStatic(SecurityUtils.class);
        mockedSecurityUtils.when(SecurityUtils::getCurrentUsername).thenReturn(USERNAME);

        testUser = User.builder().id(1L).username(USERNAME).build();
        testWallet = Wallet.builder().id(1L).name("My Wallet").balance(BigDecimal.ZERO).build();
        testMembership = WalletMembership.builder().id(1L).wallet(testWallet).user(testUser).build();
    }

    @AfterEach
    void tearDown() {
        mockedSecurityUtils.close();
    }

    @Test
    void getMyWallets_ShouldReturnList() {
        when(walletMembershipRepository.findAllByUserUsername(USERNAME)).thenReturn(List.of(testMembership));
        when(walletMapper.toDto(any(), any())).thenReturn(WalletResponseDto.builder().build());

        List<WalletResponseDto> result = walletService.getMyWallets();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void initializeDefaultWallet_ShouldSaveWalletAndMembership() {
        when(walletMapper.createDefaultWalletEntity(testUser)).thenReturn(testWallet);
        when(walletRepository.save(any())).thenReturn(testWallet);

        walletService.initializeDefaultWallet(testUser);

        verify(walletRepository).save(any());
        verify(walletMembershipRepository).save(any());
    }

    @Test
    void createWallet_Success() {
        WalletCreateDto request = WalletCreateDto.builder().name("New Wallet").build();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(walletMembershipRepository.existsByWalletNameAndUserUsername(anyString(), anyString())).thenReturn(false);
        when(walletMapper.toEntity(any(), any())).thenReturn(testWallet);
        when(walletRepository.save(any())).thenReturn(testWallet);
        when(walletMembershipRepository.save(any())).thenReturn(testMembership);
        when(walletMapper.toDto(any(), any())).thenReturn(WalletResponseDto.builder().name("New Wallet").build());

        WalletResponseDto result = walletService.createWallet(request);

        assertNotNull(result);
        verify(walletRepository).save(any());
        verify(walletMembershipRepository).save(any());
    }

    @Test
    void createWallet_ThrowsDuplicateException_WhenNameExists() {
        WalletCreateDto request = WalletCreateDto.builder().name("Existing").build();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(testUser));
        when(walletMembershipRepository.existsByWalletNameAndUserUsername("Existing", USERNAME)).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> walletService.createWallet(request));
    }

    @Test
    void updateWallet_Success() {
        WalletUpdateDto request = WalletUpdateDto.builder().name("Updated Name").build();
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(testWallet);
        when(walletRepository.save(any())).thenReturn(testWallet);
        when(walletMembershipRepository.findByWalletIdAndUserUsername(1L, USERNAME)).thenReturn(Optional.of(testMembership));
        when(walletMapper.toDto(any(), any())).thenReturn(WalletResponseDto.builder().build());

        walletService.updateWallet(1L, request);

        assertEquals("Updated Name", testWallet.getName());
        verify(walletRepository).save(testWallet);
    }

    @Test
    void deleteWallet_Success() {
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(testWallet);

        walletService.deleteWallet(1L);

        verify(walletMembershipRepository).deleteAllByWalletId(1L);
        verify(walletRepository).delete(testWallet);
    }

    @Test
    void deleteWallet_ThrowsException_WhenBalanceNotEmpty() {
        testWallet.setBalance(new BigDecimal("10.00"));
        when(transactionValidationHelper.getWalletIfOwner(1L)).thenReturn(testWallet);

        assertThrows(WalletNotEmptyException.class, () -> walletService.deleteWallet(1L));
        verify(walletRepository, never()).delete(any());
    }
}