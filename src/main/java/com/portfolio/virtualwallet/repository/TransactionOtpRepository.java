package com.portfolio.virtualwallet.repository;

import com.portfolio.virtualwallet.entity.TransactionOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionOtpRepository extends JpaRepository<TransactionOtp, Long> {

    Optional<TransactionOtp> findByTransactionId(Long transactionId);

    void deleteByExpirationTimeBefore(LocalDateTime time);
}