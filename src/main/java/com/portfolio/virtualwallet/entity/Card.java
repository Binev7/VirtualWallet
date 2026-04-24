package com.portfolio.virtualwallet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cards")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_holder", nullable = false)
    private String cardHolder;

    @Column(name = "stripe_payment_method_id", unique = true)
    private String stripePaymentMethodId;

    @Column(name = "last4", length = 4)
    private String last4;

    @Column(name = "brand")
    private String brand;

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}