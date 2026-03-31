package com.portfolio.virtualwallet.repository.specification;

import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.portfolio.virtualwallet.utils.AppConstants.EntityFields.*;

public class TransactionSpecification {

    public static Specification<Transaction> getHistorySpecification(
            Long walletId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            TransactionType type,
            TransactionStatus status) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Predicate isSender = cb.equal(root.get(SENDER_WALLET).get(ID), walletId);
            Predicate isReceiver = cb.equal(root.get(RECEIVER_WALLET).get(ID), walletId);
            predicates.add(cb.or(isSender, isReceiver));

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(CREATED_AT), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(CREATED_AT), endDate));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get(TYPE), type));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get(STATUS), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}