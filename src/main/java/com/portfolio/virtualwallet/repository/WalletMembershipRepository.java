package com.portfolio.virtualwallet.repository;

import com.portfolio.virtualwallet.entity.WalletMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletMembershipRepository extends JpaRepository<WalletMembership, Long> {
    List<WalletMembership> findByUserUsername(String username);

    Optional<WalletMembership> findByUserUsernameAndIsDefaultTrue(String username);

    Optional<WalletMembership> findByWalletIdAndUserUsername(Long walletId, String username);

    List<WalletMembership> findByWalletId(Long walletId);

    boolean existsByWalletNameAndUserUsername(String walletName, String username);
}
