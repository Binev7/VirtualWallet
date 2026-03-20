package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.card.CardCreateDto;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.mapper.CardMapper;
import com.portfolio.virtualwallet.repository.CardRepository;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.service.interfaces.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        if (cardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new DuplicateEntityException(ALREADY_EXISTS);
        }

        Card card = cardMapper.toEntity(request);
        card.setUser(user);

        Card savedCard = cardRepository.save(card);

        return cardMapper.toDto(savedCard);
    }
}