package com.portfolio.virtualwallet.repository.specification;

import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.portfolio.virtualwallet.utils.AppConstants.EntityFields.*;
import static com.portfolio.virtualwallet.utils.AppConstants.History.*;

public class AdminTransactionSpecification {

    public static Specification<Transaction> adminSearch(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String username,
            String direction,
            TransactionType type,
            TransactionStatus status) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

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

            if (username != null && !username.isBlank()) {
                Predicate isSender = cb.equal(root.get(SENDER_WALLET).get(OWNER).get(USERNAME), username);
                Predicate isReceiver = cb.equal(root.get(RECEIVER_WALLET).get(OWNER).get(USERNAME), username);

                if (INCOMING_DIRECTION.equalsIgnoreCase(direction)) {
                    predicates.add(isReceiver);
                } else if (OUTGOING_DIRECTION.equalsIgnoreCase(direction)) {
                    predicates.add(isSender);
                } else {
                    predicates.add(cb.or(isSender, isReceiver));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}