package com.portfolio.virtualwallet.entity.dto.wallet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletMemberDto {
    private Long userId;
    private String username;
    private String email;
    private boolean canSpend;
    private boolean canAddMoney;
    private boolean isOwner;
}