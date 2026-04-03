package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.WithdrawalRequestDto;
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
class WithdrawalServiceImplTest {

    @Mock
    private TransactionValidationHelper validationHelper;

    @Mock
    private BankingGatewayClient bankingClient;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WithdrawalServiceImpl withdrawalService;

    private User user;
    private Wallet wallet;
    private Card card;
    private WithdrawalRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testUser").build();
        wallet = Wallet.builder().id(1L).balance(new BigDecimal("100.00")).build();
        card = Card.builder().id(1L).build();
        requestDto = WithdrawalRequestDto.builder()
                .walletId(1L)
                .cardId(1L)
                .amount(new BigDecimal("40.00"))
                .build();
    }

    @Test
    void withdrawFromWallet_ShouldSucceed_WhenBankApproves() {
        BankApiResponse bankResponse = BankApiResponse.builder()
                .isSuccess(true)
                .transactionReference("Approved")
                .build();

        Transaction transaction = Transaction.builder().id(100L).build();

        when(validationHelper.getWalletIfOwner(1L)).thenReturn(wallet);
        when(validationHelper.getCardIfOwner(1L)).thenReturn(card);
        when(bankingClient.processWithdrawal(eq(card), any(BigDecimal.class))).thenReturn(bankResponse);
        when(transactionMapper.createWithdrawalEntity(any(), any(), any())).thenReturn(transaction);
        when(transactionMapper.toResponseDto(any(), any())).thenReturn(TransactionResponseDto.builder().build());

        TransactionResponseDto result = withdrawalService.withdrawFromWallet(user, requestDto);

        assertNotNull(result);
        assertEquals(new BigDecimal("60.00"), wallet.getBalance());
        verify(validationHelper).verifyUserCanMakeTransactions(user);
        verify(validationHelper).verifySufficientFunds(wallet, requestDto.getAmount());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void withdrawFromWallet_ShouldThrowException_WhenBankRejects() {
        BankApiResponse bankResponse = BankApiResponse.builder()
                .isSuccess(false)
                .transactionReference("Rejected")
                .build();

        when(validationHelper.getWalletIfOwner(1L)).thenReturn(wallet);
        when(validationHelper.getCardIfOwner(1L)).thenReturn(card);
        when(bankingClient.processWithdrawal(any(), any())).thenReturn(bankResponse);

        assertThrows(IllegalArgumentException.class, () -> withdrawalService.withdrawFromWallet(user, requestDto));

        verify(transactionRepository, never()).save(any());
        assertEquals(new BigDecimal("100.00"), wallet.getBalance());
    }
}
