package com.portfolio.virtualwallet.entity.dto.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Card.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardCreateDto {

    @NotBlank(message = STRIPE_PAYMENT_METHOD)
    private String stripePaymentMethodId;

    @NotBlank(message = CARD_HOLDER_NOT_BLANK)
    @Size(min = 2, max = 30, message = CARD_HOLDER_SIZE)
    private String cardHolder;
}