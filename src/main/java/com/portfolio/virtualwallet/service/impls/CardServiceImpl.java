package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.card.CardCreateDto;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import com.portfolio.virtualwallet.entity.dto.card.CardUpdateDto;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.ExceptionMessages;
import com.portfolio.virtualwallet.mapper.CardMapper;
import com.portfolio.virtualwallet.repository.CardRepository;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.service.interfaces.CardService;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.User.*;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.Card.*;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    @Override
    public CardResponseDto addCard(CardCreateDto request) {

        String currentUsername = SecurityUtils.getCurrentUsername();

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND));

        if (cardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new DuplicateEntityException(ALREADY_EXISTS);
        }

        Card card = cardMapper.toEntity(request);
        card.setUser(user);

        Card savedCard = cardRepository.save(card);

        return cardMapper.toDto(savedCard);
    }

    @Override
    public List<CardResponseDto> getAllMyCards() {
        String username = SecurityUtils.getCurrentUsername();

        return cardRepository.findByUserUsername(username)
                .stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CardResponseDto getCardById(Long id) {
        Card card = getCardIfOwned(id);
        return cardMapper.toDto(card);
    }

    @Override
    public CardResponseDto updateCard(Long id, CardUpdateDto request) {
        Card card = getCardIfOwned(id);

        card.setExpirationDate(request.getExpirationDate());
        card.setCardHolder(request.getCardHolder());

        Card updatedCard = cardRepository.save(card);
        return cardMapper.toDto(updatedCard);
    }

    @Override
    public void deleteCard(Long id) {
        Card card = getCardIfOwned(id);
        cardRepository.delete(card);
    }

    @Override
    public Card getCardIfOwned(Long cardId) {
        String username = SecurityUtils.getCurrentUsername();

        return cardRepository.findByIdAndUserUsername(cardId, username)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Card.NOT_FOUND));
    }
}