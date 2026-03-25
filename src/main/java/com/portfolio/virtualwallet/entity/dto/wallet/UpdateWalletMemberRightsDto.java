package com.portfolio.virtualwallet.entity.dto.wallet;

import lombok.Data;

@Data
public class UpdateWalletMemberRightsDto {
    private boolean canSpend;
    private boolean canAddMoney;
}