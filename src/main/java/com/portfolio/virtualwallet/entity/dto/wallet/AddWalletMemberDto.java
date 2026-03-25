package com.portfolio.virtualwallet.entity.dto.wallet;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.*;

@Data
public class AddWalletMemberDto {
    @NotBlank(message = EMAIL_NOT_BLANK)
    @Email(message = EMAIL_INVALID)
    private String userEmail;

    private boolean canSpend;
    private boolean canAddMoney;
}