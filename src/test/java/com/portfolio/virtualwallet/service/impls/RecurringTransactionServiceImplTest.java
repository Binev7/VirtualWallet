package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.RecurringTransaction;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.RecurringTransactionRequestDto;
import com.portfolio.virtualwallet.entity.enums.RecurringInterval;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.UnauthorizedException;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.repository.RecurringTransactionRepository;
import com.portfolio.virtualwallet.utils.TransactionHelper;
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecurringTransactionServiceImplTest {

    @Mock
    private RecurringTransactionRepository recurringRepository;
    @Mock
    private TransactionValidationHelper validationHelper;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private TransactionHelper transactionHelper;

    @InjectMocks
    private RecurringTransactionServiceImpl recurringService;

    private User currentUser;
    private Wallet senderWallet;
    private Wallet receiverWallet;
    private RecurringTransactionRequestDto requestDto;
    private RecurringTransaction recurringTx;

    @BeforeEach
    void setUp() {
        currentUser = User.builder().id(1L).build();
        senderWallet = Wallet.builder().id(10L).owner(currentUser).build();
        receiverWallet = Wallet.builder().id(20L).build();

        requestDto = RecurringTransactionRequestDto.builder()
                .senderWalletId(10L)
                .receiverWalletId(20L)
                .amount(BigDecimal.TEN)
                .interval(RecurringInterval.WEEKLY)
                .build();

        recurringTx = RecurringTransaction.builder()
                .id(1L)
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .build();
    }

    @Test
    void createRecurringTransfer_ShouldSucceed() {
        LocalDateTime nextExec = LocalDateTime.now().plusDays(7);
        when(validationHelper.getWalletIfOwner(10L)).thenReturn(senderWallet);
        when(validationHelper.getWalletById(20L)).thenReturn(receiverWallet);
        when(transactionHelper.calculateNextExecutionTime(RecurringInterval.WEEKLY)).thenReturn(nextExec);
        when(transactionMapper.createRecurringTransferEntity(any(), any(), any(), any())).thenReturn(recurringTx);

        recurringService.createRecurringTransfer(currentUser, requestDto);

        verify(validationHelper).verifyUserCanMakeTransactions(currentUser);
        verify(recurringRepository).save(recurringTx);
    }

    @Test
    void cancelRecurringTransfer_ShouldSucceed_WhenOwner() {
        when(recurringRepository.findById(1L)).thenReturn(Optional.of(recurringTx));

        recurringService.cancelRecurringTransfer(currentUser, 1L);

        verify(recurringRepository).delete(recurringTx);
    }

    @Test
    void cancelRecurringTransfer_ShouldThrowNotFound_WhenTxMissing() {
        when(recurringRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> recurringService.cancelRecurringTransfer(currentUser, 1L));
        verify(recurringRepository, never()).delete(any());
    }

    @Test
    void cancelRecurringTransfer_ShouldThrowUnauthorized_WhenNotOwner() {
        User differentUser = User.builder().id(99L).build();
        when(recurringRepository.findById(1L)).thenReturn(Optional.of(recurringTx));

        assertThrows(UnauthorizedException.class, () -> recurringService.cancelRecurringTransfer(differentUser, 1L));
        verify(recurringRepository, never()).delete(any());
    }
}
