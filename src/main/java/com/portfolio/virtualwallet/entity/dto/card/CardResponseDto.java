package com.portfolio.virtualwallet.entity.dto.card;

import lombok.Data;

@Data
public class CardResponseDto {
    private Long id;
    private String brand;
    private String last4;
    private String expirationDate;
    private String cardHolder;
}