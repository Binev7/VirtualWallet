package com.portfolio.virtualwallet.entity.dto.card;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CardResponseDto {
    private Long id;
    private String maskedCardNumber;
    private LocalDate expirationDate;
    private String cardHolder;
}