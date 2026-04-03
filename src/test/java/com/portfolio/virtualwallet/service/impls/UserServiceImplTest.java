package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.automation.event.OnRegistrationCompleteEvent;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.VerificationToken;
import com.portfolio.virtualwallet.entity.dto.user.UserDetailsAdminDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.mapper.UserMapper;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.VerificationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "appBaseUrl", BASE_URL);
        testUser = User.builder()
                .id(1L)
                .username("ivan")
                .email("ivan@test.com")
                .build();
    }

    @Test
    @SuppressWarnings("unchecked")
    void searchPublicUsers_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(testUser));

        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(userPage);
        when(userMapper.toPublicDto(any())).thenReturn(UserPublicResponseDto.builder().build());

        Page<UserPublicResponseDto> result = userService.searchPublicUsers("term", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @SuppressWarnings("unchecked")
    void adminSearchUsers_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(testUser));

        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(userPage);
        when(userMapper.toAdminDto(any())).thenReturn(UserDetailsAdminDto.builder().build());

        Page<UserDetailsAdminDto> result = userService.adminSearchUsers("term", pageable);

        assertNotNull(result);
        verify(userRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void toggleUserBlockStatus_ShouldUpdateStatus() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        userService.toggleUserBlockStatus(1L, true);

        assertTrue(testUser.isBlocked());
        verify(userRepository).save(testUser);
    }

    @Test
    void toggleUserBlockStatus_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.toggleUserBlockStatus(1L, true));
    }

    @Test
    void changeEmail_ShouldSucceed_WhenEmailIsUnique() {
        String newEmail = "new@test.com";
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any())).thenReturn(testUser);
        when(userMapper.createVerificationToken(any(), anyString())).thenReturn(new VerificationToken());

        userService.changeEmail(testUser, newEmail);

        assertEquals(newEmail, testUser.getEmail());
        assertFalse(testUser.isEmailVerified());
        verify(userRepository).save(testUser);
        verify(tokenRepository).save(any());
        verify(eventPublisher).publishEvent(any(OnRegistrationCompleteEvent.class));
    }

    @Test
    void changeEmail_ShouldThrowException_WhenEmailExists() {
        String newEmail = "exists@test.com";
        when(userRepository.existsByEmail(newEmail)).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> userService.changeEmail(testUser, newEmail));
        verify(userRepository, never()).save(any());
    }

    @Test
    void changeEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.changeEmail(testUser, "new@test.com"));
    }
}
