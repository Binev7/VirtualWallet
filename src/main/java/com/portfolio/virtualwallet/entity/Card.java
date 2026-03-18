package com.portfolio.virtualwallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    @Pattern(regexp = "^\\d{16}$", message = "Card number must be exactly 16 digits.")
    private String cardNumber;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    @Size(min = 2, max = 30, message = "Card holder must be between 2 and 30 symbols.")
    private String cardHolder;

    @Column(nullable = false, length = 3)
    @Pattern(regexp = "^\\d{3}$", message = "Check number must be exactly 3 digits.")
    private String checkNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
