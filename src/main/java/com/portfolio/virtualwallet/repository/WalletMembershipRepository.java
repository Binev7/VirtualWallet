package com.portfolio.virtualwallet.repository;

import com.portfolio.virtualwallet.entity.WalletMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletMembershipRepository extends JpaRepository<WalletMembership, Long> {

    List<WalletMembership> findAllByUserUsername(String username);

    List<WalletMembership> findAllByWalletId(Long walletId);

    Optional<WalletMembership> findByWalletIdAndUserId(Long walletId, Long userId);

    boolean existsByWalletNameAndUserUsername(String walletName, String username);

    void deleteAllByWalletId(Long walletId);

    Optional<WalletMembership> findByWalletIdAndUserUsername(Long walletId, String username);

    Optional<WalletMembership> findByUserUsernameAndIsDefaultTrue(String username);
}
