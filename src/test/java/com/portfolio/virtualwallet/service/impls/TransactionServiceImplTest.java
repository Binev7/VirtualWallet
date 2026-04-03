package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.automation.event.OnLargeTransactionEvent;
import com.portfolio.virtualwallet.automation.event.OnTransactionSuccessEvent;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.TransactionOtp;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.*;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.repository.TransactionOtpRepository;
import com.portfolio.virtualwallet.repository.TransactionRepository;
import com.portfolio.virtualwallet.utils.TransactionHelper;
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock private TransactionValidationHelper validationHelper;
    @Mock private TransactionHelper transactionHelper;
    @Mock private TransactionMapper transactionMapper;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private TransactionOtpRepository otpRepository;
    @Mock private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User currentUser;
    private Wallet senderWallet;
    private Wallet receiverWallet;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(transactionService, "largeAmountThreshold", new BigDecimal("1000.00"));

        currentUser = User.builder().id(1L).email("user@test.com").build();
        senderWallet = Wallet.builder().id(10L).balance(new BigDecimal("2000.00")).build();
        receiverWallet = Wallet.builder().id(20L).build();
        transaction = Transaction.builder().id(100L).senderWallet(senderWallet).receiverWallet(receiverWallet).build();
    }

    @Test
    void transfer_SmallAmount_ShouldExecuteImmediately() {
        TransferRequestDto request = TransferRequestDto.builder()
                .senderWalletId(10L)
                .receiverWalletId(20L)
                .amount(new BigDecimal("100.00"))
                .build();

        when(validationHelper.getWalletIfOwner(10L)).thenReturn(senderWallet);
        when(validationHelper.getWalletById(20L)).thenReturn(receiverWallet);
        when(transactionMapper.createTransferEntity(any(), any(), any())).thenReturn(transaction);
        when(transactionMapper.toResponseDto(any(), any())).thenReturn(TransactionResponseDto.builder().build());

        transactionService.transfer(currentUser, request);

        verify(transactionHelper).executeMoneyTransfer(transaction, senderWallet, receiverWallet);
        verify(eventPublisher).publishEvent(any(OnTransactionSuccessEvent.class));
    }

    @Test
    void transfer_LargeAmount_ShouldCreateOtp() {
        TransferRequestDto request = TransferRequestDto.builder()
                .senderWalletId(10L)
                .receiverWalletId(20L)
                .amount(new BigDecimal("1500.00"))
                .build();

        TransactionOtp otp = TransactionOtp.builder().otpCode("123456").build();

        when(validationHelper.getWalletIfOwner(10L)).thenReturn(senderWallet);
        when(validationHelper.getWalletById(20L)).thenReturn(receiverWallet);
        when(transactionMapper.createTransferEntity(any(), any(), any())).thenReturn(transaction);
        when(transactionHelper.createAndSaveOtp(transaction)).thenReturn(otp);
        when(transactionMapper.toResponseDto(any(), any())).thenReturn(TransactionResponseDto.builder().build());

        transactionService.transfer(currentUser, request);

        verify(transactionHelper, never()).executeMoneyTransfer(any(), any(), any());
        verify(eventPublisher).publishEvent(any(OnLargeTransactionEvent.class));
    }

    @Test
    void verifyOtp_Success() {
        // Използваме твоето DTO с Builder
        OtpVerificationRequestDto request = OtpVerificationRequestDto.builder()
                .transactionId(100L)
                .otpCode("123456")
                .build();

        TransactionOtp otp = TransactionOtp.builder()
                .otpCode("123456")
                .transaction(transaction)
                .expirationTime(LocalDateTime.now().plusMinutes(5))
                .build();

        when(otpRepository.findByTransactionId(100L)).thenReturn(Optional.of(otp));
        when(transactionMapper.toResponseDto(any(), any())).thenReturn(TransactionResponseDto.builder().build());

        transactionService.verifyOtp(currentUser, request);

        verify(transactionHelper).executeMoneyTransfer(transaction, senderWallet, receiverWallet);
        verify(otpRepository).delete(otp);
    }

    @Test
    void verifyOtp_Expired_ShouldFailTransaction() {
        OtpVerificationRequestDto request = OtpVerificationRequestDto.builder()
                .transactionId(100L)
                .otpCode("123456")
                .build();

        TransactionOtp otp = TransactionOtp.builder()
                .otpCode("123456")
                .transaction(transaction)
                .expirationTime(LocalDateTime.now().minusMinutes(1))
                .build();

        when(otpRepository.findByTransactionId(100L)).thenReturn(Optional.of(otp));

        assertThrows(IllegalArgumentException.class, () -> transactionService.verifyOtp(currentUser, request));
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
        verify(transactionRepository).save(transaction);
        verify(otpRepository).delete(otp);
    }

    @Test
    void verifyOtp_InvalidCode_ShouldThrowException() {
        OtpVerificationRequestDto request = OtpVerificationRequestDto.builder()
                .transactionId(100L)
                .otpCode("wrong")
                .build();

        TransactionOtp otp = TransactionOtp.builder()
                .otpCode("123456")
                .transaction(transaction)
                .expirationTime(LocalDateTime.now().plusMinutes(5))
                .build();

        when(otpRepository.findByTransactionId(100L)).thenReturn(Optional.of(otp));

        assertThrows(IllegalArgumentException.class, () -> transactionService.verifyOtp(currentUser, request));
        verify(transactionHelper, never()).executeMoneyTransfer(any(), any(), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getWalletHistory_ShouldReturnPage() {
        Page<Transaction> page = new PageImpl<>(List.of(transaction));
        when(transactionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(transactionMapper.toHistoryDto(any(), anyLong())).thenReturn(new TransactionHistoryDto());

        Page<TransactionHistoryDto> result = transactionService.getWalletHistory(
                currentUser, 10L, null, null, null, null, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(validationHelper).getWalletIfOwner(10L);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getGlobalTransactionsForAdmin_ShouldReturnPage() {
        Page<Transaction> page = new PageImpl<>(List.of(transaction));
        when(transactionRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(transactionMapper.toAdminDto(any())).thenReturn(new TransactionAdminDto());

        Page<TransactionAdminDto> result = transactionService.getGlobalTransactionsForAdmin(
                null, null, null, null, null, null, 0, 10, "createdAt");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}
