package com.portfolio.virtualwallet.repository;

import com.portfolio.virtualwallet.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByCardNumber(String cardNumber);

    List<Card> findByUserUsername(String username);

    Optional<Card> findByIdAndUserUsername(Long id, String username);
}
