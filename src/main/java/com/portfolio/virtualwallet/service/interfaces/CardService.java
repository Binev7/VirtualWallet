package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.dto.card.CardCreateDto;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import com.portfolio.virtualwallet.entity.dto.card.CardUpdateDto;

import java.util.List;

public interface CardService {
    CardResponseDto addCard(CardCreateDto request);

    List<CardResponseDto> getAllMyCards();

    CardResponseDto getCardById(Long id);

    CardResponseDto updateCard(Long id, CardUpdateDto request);

    void deleteCard(Long id);

    Card getCardIfOwned(Long cardId);
}