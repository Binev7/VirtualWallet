package com.portfolio.virtualwallet.repository;

import com.portfolio.virtualwallet.entity.RecurringTransaction;
import com.portfolio.virtualwallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {

    @Query("SELECT rt FROM RecurringTransaction rt WHERE rt.isActive = true AND rt.nextExecutionTime <= :currentTime")
    List<RecurringTransaction> findAllDueTransactions(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT rt FROM RecurringTransaction rt WHERE rt.senderWallet.id = :walletId AND rt.isActive = true")
    List<RecurringTransaction> findActiveSubscriptionsByWallet(@Param("walletId") Long walletId);

    @Query("SELECT rt FROM RecurringTransaction rt WHERE rt.senderWallet.owner = :user AND rt.isActive = true")
    List<RecurringTransaction> findAllActiveByUser(@Param("user") User user);
}