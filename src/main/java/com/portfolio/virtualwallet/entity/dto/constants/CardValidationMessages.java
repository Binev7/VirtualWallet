package com.portfolio.virtualwallet.entity.dto.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CardValidationMessages {

    public static final String CARD_NUMBER_NOT_BLANK = "Card number cannot be empty.";
    public static final String CARD_NUMBER_PATTERN = "Card number must be exactly 16 digits.";

    public static final String EXPIRATION_DATE_NOT_NULL = "Expiration date is required.";
    public static final String EXPIRATION_DATE_FUTURE = "Expiration date must be in the future.";

    public static final String CARD_HOLDER_NOT_BLANK = "Card holder name cannot be empty.";
    public static final String CARD_HOLDER_SIZE = "Card holder must be between 2 and 30 symbols.";

    public static final String CHECK_NUMBER_NOT_BLANK = "Check number (CVV) cannot be empty.";
    public static final String CHECK_NUMBER_PATTERN = "Check number must be exactly 3 digits.";
}