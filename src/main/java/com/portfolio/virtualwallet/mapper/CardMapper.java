package com.portfolio.virtualwallet.mapper;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.dto.card.CardCreateDto;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public Card toEntity(CardCreateDto dto) {
        Card card = new Card();
        card.setCardNumber(dto.getCardNumber());
        card.setExpirationDate(dto.getExpirationDate());
        card.setCardHolder(dto.getCardHolder());
        card.setCheckNumber(dto.getCheckNumber());

        return card;
    }

    public CardResponseDto toDto(Card entity) {
        CardResponseDto dto = new CardResponseDto();
        dto.setId(entity.getId());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setCardHolder(entity.getCardHolder());

        dto.setMaskedCardNumber(maskCardNumber(entity.getCardNumber()));

        return dto;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return cardNumber;
        }
        return "**** **** **** " + cardNumber.substring(12);
    }
}