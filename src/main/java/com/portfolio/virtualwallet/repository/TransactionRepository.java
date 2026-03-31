package com.portfolio.virtualwallet.repository;

import com.portfolio.virtualwallet.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Query("SELECT t FROM Transaction t WHERE t.senderWallet.id = :walletId OR t.receiverWallet.id = :walletId ORDER BY t.createdAt DESC")
    List<Transaction> getWalletHistory(@Param("walletId") Long walletId);

    @Query("SELECT t FROM Transaction t WHERE t.card.id = :cardId ORDER BY t.createdAt DESC")
    List<Transaction> getCardHistory(@Param("cardId") Long cardId);

    Page<Transaction> findAllBySenderWalletIdOrReceiverWalletIdAndCreatedAtAfter(
            Long senderId,
            Long receiverId,
            LocalDateTime startDate,
            Pageable pageable
    );
}