package com.portfolio.virtualwallet.repository.specification;

import com.portfolio.virtualwallet.entity.User;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import static com.portfolio.virtualwallet.utils.AppConstants.EntityFields.*;

public class UserSpecification {

    public static Specification<User> searchUsers(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + searchTerm.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get(USERNAME)), pattern),
                    cb.like(cb.lower(root.get(EMAIL)), pattern),
                    cb.like(root.get(PHONE_NUMBER), pattern)
            );
        };
    }

    public static Specification<User> searchUsers(String searchTerm, String excludeUsername) {
        return (root, query, cb) -> {

            Predicate notMe = cb.notEqual(root.get(USERNAME), excludeUsername);

            if (searchTerm == null || searchTerm.isBlank()) {
                return notMe;
            }

            String pattern = "%" + searchTerm.toLowerCase() + "%";

            Predicate searchPredicate = cb.or(
                    cb.like(cb.lower(root.get(USERNAME)), pattern),
                    cb.like(cb.lower(root.get(EMAIL)), pattern),
                    cb.like(root.get(PHONE_NUMBER), pattern)
            );

            return cb.and(searchPredicate, notMe);
        };
    }
}