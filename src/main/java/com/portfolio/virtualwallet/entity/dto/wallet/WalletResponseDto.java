package com.portfolio.virtualwallet.entity.dto.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletResponseDto {
    private Long id;
    private String name;
    private BigDecimal balance;

    @JsonProperty("isJoint")
    private boolean isJoint;

    @JsonProperty("isDefault")
    private boolean isDefault;

    private boolean isOwner;
}