package com.portfolio.virtualwallet.entity.dto.wallet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Wallet.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletUpdateDto {
    @NotBlank(message = WALLET_NAME_NOT_BLANK)
    @Size(min = 2, max = 30, message = WALLET_NAME_SIZE)
    private String name;
}