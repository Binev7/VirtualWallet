package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.DepositRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.integration.bank.BankApiResponse;
import com.portfolio.virtualwallet.integration.bank.BankingGatewayClient;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.repository.TransactionRepository;
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositServiceImplTest {

    @Mock
    private TransactionValidationHelper validationHelper;
    @Mock
    private BankingGatewayClient bankingClient;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DepositServiceImpl depositService;

    private User user;
    private Wallet wallet;
    private Card card;
    private DepositRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testUser").build();
        wallet = Wallet.builder().id(1L).balance(new BigDecimal("100.00")).build();
        card = Card.builder().id(1L).build();
        requestDto = DepositRequestDto.builder()
                .walletId(1L)
                .cardId(1L)
                .amount(new BigDecimal("50.00"))
                .build();
    }

    @Test
    void depositToWallet_ShouldSucceed_WhenBankApproves() {
        BankApiResponse bankResponse = BankApiResponse.builder()
                .isSuccess(true)
                .transactionReference("Success")
                .build();

        Transaction transaction = Transaction.builder().id(1L).build();

        when(validationHelper.getWalletIfOwner(1L)).thenReturn(wallet);
        when(validationHelper.getCardIfOwner(1L)).thenReturn(card);
        when(bankingClient.processDeposit(eq(card), any(BigDecimal.class))).thenReturn(bankResponse);

        when(transactionMapper.createDepositEntity(any(), any(), any())).thenReturn(transaction);
        when(transactionMapper.toResponseDto(any(), any())).thenReturn(TransactionResponseDto.builder().build());

        TransactionResponseDto result = depositService.depositToWallet(user, requestDto);

        assertNotNull(result);
        assertEquals(new BigDecimal("150.00"), wallet.getBalance());
        verify(validationHelper).verifyUserCanMakeTransactions(user);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void depositToWallet_ShouldThrowException_WhenBankRejects() {
        BankApiResponse bankResponse = BankApiResponse.builder()
                .isSuccess(false)
                .transactionReference("Rejected")
                .build();

        when(validationHelper.getWalletIfOwner(1L)).thenReturn(wallet);
        when(validationHelper.getCardIfOwner(1L)).thenReturn(card);
        when(bankingClient.processDeposit(any(), any())).thenReturn(bankResponse);

        assertThrows(IllegalArgumentException.class, () -> depositService.depositToWallet(user, requestDto));
        verify(transactionRepository, never()).save(any());
        assertEquals(new BigDecimal("100.00"), wallet.getBalance());
    }
}