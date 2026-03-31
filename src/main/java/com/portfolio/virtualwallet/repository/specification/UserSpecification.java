package com.portfolio.virtualwallet.repository.specification;

import com.portfolio.virtualwallet.entity.User;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

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
}