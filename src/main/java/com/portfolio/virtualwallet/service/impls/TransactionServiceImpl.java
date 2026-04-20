package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.automation.event.OnLargeTransactionEvent;
import com.portfolio.virtualwallet.automation.event.OnTransactionSuccessEvent;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.TransactionOtp;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.dto.transaction.*;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.UnauthorizedException;
import com.portfolio.virtualwallet.mapper.TransactionMapper;
import com.portfolio.virtualwallet.repository.TransactionOtpRepository;
import com.portfolio.virtualwallet.repository.TransactionRepository;
import com.portfolio.virtualwallet.repository.WalletMembershipRepository;
import com.portfolio.virtualwallet.repository.specification.AdminTransactionSpecification;
import com.portfolio.virtualwallet.repository.specification.TransactionSpecification;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import com.portfolio.virtualwallet.utils.AppConstants;
import com.portfolio.virtualwallet.utils.TransactionHelper;
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.Transaction.*;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.Wallet.WALLET_NOT_OWNER;
import static com.portfolio.virtualwallet.utils.AppConstants.EntityFields.CREATED_AT;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.OTP_SENT;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.TRANSFER_COMPLETED;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionValidationHelper validationHelper;
    private final TransactionHelper transactionHelper;
    private final TransactionMapper transactionMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final TransactionOtpRepository otpRepository;
    private final TransactionRepository transactionRepository;
    private final WalletMembershipRepository walletMembershipRepository;

    @Value("${app.transaction.large-amount-threshold}")
    private BigDecimal largeAmountThreshold;

    @Override
    @Transactional
    public TransactionResponseDto transfer(User currentUser, TransferRequestDto request) {
        validationHelper.verifyUserCanMakeTransactions(currentUser);

        Wallet senderWallet = validationHelper.getWalletIfOwner(request.getSenderWalletId());
        Wallet receiverWallet = validationHelper.getWalletById(request.getReceiverWalletId());

        validationHelper.verifySufficientFunds(senderWallet, request.getAmount());

        Transaction transaction = transactionMapper.createTransferEntity(request.getAmount(), senderWallet, receiverWallet);

        if (request.getAmount().compareTo(largeAmountThreshold) > 0) {
            TransactionOtp otp = transactionHelper.createAndSaveOtp(transaction);
            eventPublisher.publishEvent(new OnLargeTransactionEvent(otp, currentUser.getEmail()));

            return transactionMapper.toResponseDto(transaction, OTP_SENT);
        } else {
            transactionHelper.executeMoneyTransfer(transaction, senderWallet, receiverWallet);
            eventPublisher.publishEvent(new OnTransactionSuccessEvent(transaction));

            return transactionMapper.toResponseDto(transaction, TRANSFER_COMPLETED);
        }
    }

    @Override
    @Transactional
    public TransactionResponseDto verifyOtp(User currentUser, OtpVerificationRequestDto request) {
        TransactionOtp otp = otpRepository.findByTransactionId(request.getTransactionId())
                .orElseThrow(() -> new EntityNotFoundException(OTP_NOT_FOUND));

        Transaction transaction = otp.getTransaction();

        validationHelper.getWalletIfOwner(transaction.getSenderWallet().getId());

        if (otp.isExpired()) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            otpRepository.delete(otp);
            throw new IllegalArgumentException(OTP_EXPIRED);
        }

        if (!otp.getOtpCode().equals(request.getOtpCode())) {
            throw new IllegalArgumentException(OTP_INVALID);
        }

        transactionHelper.executeMoneyTransfer(transaction, transaction.getSenderWallet(), transaction.getReceiverWallet());
        otpRepository.delete(otp);
        eventPublisher.publishEvent(new OnTransactionSuccessEvent(transaction));

        return transactionMapper.toResponseDto(transaction, TRANSFER_COMPLETED);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionHistoryDto> getWalletHistory(
            User currentUser, Long walletId, LocalDateTime startDate, LocalDateTime endDate,
            TransactionType type, TransactionStatus status, int page, int size) {

        walletMembershipRepository.findByWalletIdAndUserId(walletId, currentUser.getId())
                .orElseThrow(() -> new UnauthorizedException(WALLET_NOT_OWNER));

        if (startDate == null) {
            startDate = LocalDateTime.now().minusMonths(AppConstants.History.DEFAULT_HISTORY_MONTHS);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, CREATED_AT));
        Specification<Transaction> spec = TransactionSpecification.getHistorySpecification(walletId, startDate, endDate, type, status);

        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);

        return transactions.map(tx -> transactionMapper.toHistoryDto(tx, walletId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionAdminDto> getGlobalTransactionsForAdmin(
            LocalDateTime startDate, LocalDateTime endDate,
            String username, String direction,
            TransactionType type, TransactionStatus status,
            int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));

        Specification<Transaction> spec = AdminTransactionSpecification.adminSearch(
                startDate, endDate, username, direction, type, status);

        return transactionRepository.findAll(spec, pageable)
                .map(transactionMapper::toAdminDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionHistoryDto> getUserTransactions(User currentUser, Pageable pageable) {

        List<Long> userWalletIds = walletMembershipRepository.findAllByUserUsername(currentUser.getUsername())
                .stream()
                .map(membership -> membership.getWallet().getId())
                .toList();

        if (userWalletIds.isEmpty()) {
            return Page.empty(pageable);
        }

        Specification<Transaction> spec = TransactionSpecification.getGlobalUserHistorySpecification(userWalletIds);

        return transactionRepository.findAll(spec, pageable).map(tx -> {

            Long relevantWalletId = null;

            if (tx.getReceiverWallet() != null && userWalletIds.contains(tx.getReceiverWallet().getId())) {
                relevantWalletId = tx.getReceiverWallet().getId();
            }
            else if (tx.getSenderWallet() != null && userWalletIds.contains(tx.getSenderWallet().getId())) {
                relevantWalletId = tx.getSenderWallet().getId();
            }

            return transactionMapper.toHistoryDto(tx, relevantWalletId);
        });
    }
}