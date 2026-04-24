package com.portfolio.virtualwallet.mapper;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.dto.card.CardCreateDto;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public Card toEntity(CardCreateDto dto) {
        Card card = new Card();
        card.setCardHolder(dto.getCardHolder());
        card.setStripePaymentMethodId(dto.getStripePaymentMethodId());
        return card;
    }

    public CardResponseDto toDto(Card entity) {
        CardResponseDto dto = new CardResponseDto();
        dto.setId(entity.getId());
        dto.setCardHolder(entity.getCardHolder());
        dto.setBrand(entity.getBrand());
        dto.setLast4(entity.getLast4());
        dto.setExpirationDate(entity.getExpirationDate());

        return dto;
    }
}