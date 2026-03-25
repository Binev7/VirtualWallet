package com.portfolio.virtualwallet.entity.dto.wallet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.*;

@Data
public class WalletUpdateDto {
    @NotBlank(message = WALLET_NAME_NOT_BLANK)
    @Size(min = 2, max = 30, message = WALLET_NAME_SIZE)
    private String name;
}