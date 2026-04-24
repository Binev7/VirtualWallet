package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.card.CardCreateDto;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import com.portfolio.virtualwallet.entity.dto.card.CardUpdateDto;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.mapper.CardMapper;
import com.portfolio.virtualwallet.repository.CardRepository;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import com.stripe.model.PaymentMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock private CardRepository cardRepository;
    @Mock private UserRepository userRepository;
    @Mock private CardMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private MockedStatic<SecurityUtils> mockedSecurityUtils;
    private final String TEST_USER = "testUser";

    @BeforeEach
    void setUp() {
        mockedSecurityUtils = mockStatic(SecurityUtils.class);
        mockedSecurityUtils.when(SecurityUtils::getCurrentUsername).thenReturn(TEST_USER);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityUtils.close();
    }

    @Nested
    @DisplayName("Add Card Tests")
    class AddCardTests {

        @Test
        void addCard_ShouldSucceed_WhenValidRequest() {
            CardCreateDto request = CardCreateDto.builder().stripePaymentMethodId("pm_123456").build();
            User user = User.builder().username(TEST_USER).build();
            Card card = Card.builder().stripePaymentMethodId(request.getStripePaymentMethodId()).build();

            when(userRepository.findByUsername(TEST_USER)).thenReturn(Optional.of(user));
            when(cardRepository.existsByStripePaymentMethodId(request.getStripePaymentMethodId())).thenReturn(false);
            when(cardMapper.toEntity(request)).thenReturn(card);
            when(cardRepository.save(any(Card.class))).thenReturn(card);
            when(cardMapper.toDto(any(Card.class))).thenReturn(new CardResponseDto());

            PaymentMethod mockMethod = mock(PaymentMethod.class);
            PaymentMethod.Card mockStripeCard = mock(PaymentMethod.Card.class);

            when(mockMethod.getCard()).thenReturn(mockStripeCard);
            when(mockStripeCard.getBrand()).thenReturn("visa");
            when(mockStripeCard.getLast4()).thenReturn("4242");
            when(mockStripeCard.getExpMonth()).thenReturn(12L);
            when(mockStripeCard.getExpYear()).thenReturn(2025L);

            try (MockedStatic<PaymentMethod> mockedStripe = mockStatic(PaymentMethod.class)) {
                mockedStripe.when(() -> PaymentMethod.retrieve("pm_123456")).thenReturn(mockMethod);

                CardResponseDto result = cardService.addCard(request);

                assertNotNull(result);
                verify(cardRepository).save(card);
                assertEquals(user, card.getUser());

                assertEquals("visa", card.getBrand());
                assertEquals("4242", card.getLast4());
                assertEquals("12/25", card.getExpirationDate());
            }
        }

        @Test
        void addCard_ShouldThrow_WhenStripeIdExists() {
            // ПРОМЯНА: Проверяваме по Stripe ID
            CardCreateDto request = CardCreateDto.builder().stripePaymentMethodId("pm_1111").build();
            when(userRepository.findByUsername(TEST_USER)).thenReturn(Optional.of(new User()));
            when(cardRepository.existsByStripePaymentMethodId("pm_1111")).thenReturn(true);

            assertThrows(DuplicateEntityException.class, () -> cardService.addCard(request));
        }
    }

    @Nested
    @DisplayName("Get & Update Cards Tests")
    class GetUpdateTests {
        @Test
        void getAllMyCards_ShouldReturnList() {
            Card card = new Card();
            when(cardRepository.findByUserUsername(TEST_USER)).thenReturn(List.of(card));
            when(cardMapper.toDto(card)).thenReturn(new CardResponseDto());

            List<CardResponseDto> result = cardService.getAllMyCards();

            assertEquals(1, result.size());
        }

        @Test
        void updateCard_ShouldUpdateFields_WhenOwned() {
            Long cardId = 1L;
            CardUpdateDto request = CardUpdateDto.builder()
                    .cardHolder("New Holder")
                    .expirationDate("12/29")
                    .build();

            Card existingCard = Card.builder().id(cardId).build();

            when(cardRepository.findByIdAndUserUsername(cardId, TEST_USER)).thenReturn(Optional.of(existingCard));
            when(cardRepository.save(any())).thenReturn(existingCard);
            when(cardMapper.toDto(any())).thenReturn(new CardResponseDto());

            cardService.updateCard(cardId, request);

            assertEquals("New Holder", existingCard.getCardHolder());
            assertEquals("12/29", existingCard.getExpirationDate());
            verify(cardRepository).save(existingCard);
        }

        @Test
        void getCardIfOwned_ShouldThrow_WhenNotYours() {
            when(cardRepository.findByIdAndUserUsername(99L, TEST_USER)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> cardService.getCardIfOwned(99L));
        }
    }

    @Nested
    @DisplayName("Delete Card Tests")
    class DeleteTests {
        @Test
        void deleteCard_ShouldSucceed_WhenOwned() {
            Card card = Card.builder().id(1L).build();
            when(cardRepository.findByIdAndUserUsername(1L, TEST_USER)).thenReturn(Optional.of(card));

            cardService.deleteCard(1L);

            verify(cardRepository).delete(card);
        }
    }
}