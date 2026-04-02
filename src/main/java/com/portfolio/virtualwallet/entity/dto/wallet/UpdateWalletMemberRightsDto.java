package com.portfolio.virtualwallet.entity.dto.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWalletMemberRightsDto {
    private boolean canSpend;
    private boolean canAddMoney;
}