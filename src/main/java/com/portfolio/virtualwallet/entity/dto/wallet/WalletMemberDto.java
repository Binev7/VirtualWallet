package com.portfolio.virtualwallet.entity.dto.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletMemberDto {
    private Long userId;
    private String username;
    private String email;
    private boolean canSpend;
    private boolean canAddMoney;
    private boolean isOwner;
}