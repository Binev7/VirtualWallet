package com.portfolio.virtualwallet.entity.dto.card;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

import static com.portfolio.virtualwallet.entity.dto.constants.CardValidationMessages.*;

@Data
public class CardCreateDto {

    @NotBlank(message = CARD_NUMBER_NOT_BLANK)
    @Pattern(regexp = "^\\d{16}$", message = CARD_NUMBER_PATTERN)
    private String cardNumber;

    @NotNull(message = EXPIRATION_DATE_NOT_NULL)
    @Future(message = EXPIRATION_DATE_FUTURE)
    private LocalDate expirationDate;

    @NotBlank(message = CARD_HOLDER_NOT_BLANK)
    @Size(min = 2, max = 30, message = CARD_HOLDER_SIZE)
    private String cardHolder;

    @NotBlank(message = CHECK_NUMBER_NOT_BLANK)
    @Pattern(regexp = "^\\d{3}$", message = CHECK_NUMBER_PATTERN)
    private String checkNumber;
}