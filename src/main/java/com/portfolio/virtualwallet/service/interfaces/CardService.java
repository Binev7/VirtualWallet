package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.dto.card.CardCreateDto;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;

public interface CardService {
    CardResponseDto addCard(CardCreateDto request);
}