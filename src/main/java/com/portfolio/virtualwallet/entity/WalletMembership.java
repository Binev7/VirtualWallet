package com.portfolio.virtualwallet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallet_memberships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "can_spend", nullable = false)
    private boolean canSpend;

    @Column(name = "can_add_money", nullable = false)
    private boolean canAddMoney;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;
}
