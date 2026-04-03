package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.automation.event.OnPasswordResetEvent;
import com.portfolio.virtualwallet.automation.event.OnRegistrationCompleteEvent;
import com.portfolio.virtualwallet.entity.PasswordResetToken;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.VerificationToken;
import com.portfolio.virtualwallet.entity.dto.auth.*;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.mapper.UserMapper;
import com.portfolio.virtualwallet.repository.PasswordResetTokenRepository;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.VerificationTokenRepository;
import com.portfolio.virtualwallet.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserMapper userMapper;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private VerificationTokenRepository tokenRepository;
    @Mock private PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authenticationService, "appBaseUrl", "http://localhost:8080");
    }

    @Nested
    @DisplayName("Registration Tests")
    class RegisterTests {
        @Test
        void register_ShouldSucceed_WhenDataIsValid() {
            UserRegisterDto dto = UserRegisterDto.builder()
                    .username("ivan")
                    .email("ivan@test.com")
                    .phoneNumber("0888123456")
                    .password("password")
                    .build();

            User user = User.builder().email(dto.getEmail()).build();

            when(userRepository.existsByUsername(any())).thenReturn(false);
            when(userRepository.existsByEmail(any())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(any())).thenReturn(false);
            when(userMapper.toEntity(dto)).thenReturn(user);
            when(passwordEncoder.encode(any())).thenReturn("encodedPass");
            when(userRepository.save(any())).thenReturn(user);
            when(userMapper.createVerificationToken(any(), any())).thenReturn(new VerificationToken());
            when(jwtService.generateToken(any())).thenReturn("jwt-token");

            AuthenticationResponseDto result = authenticationService.register(dto);

            assertNotNull(result);
            assertEquals("jwt-token", result.getToken());
            verify(eventPublisher).publishEvent(any(OnRegistrationCompleteEvent.class));
        }

        @Test
        void register_ShouldThrow_WhenEmailAlreadyExists() {
            UserRegisterDto dto = UserRegisterDto.builder().email("exists@test.com").build();
            when(userRepository.existsByUsername(any())).thenReturn(false);
            when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

            assertThrows(DuplicateEntityException.class, () -> authenticationService.register(dto));
        }
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {
        @Test
        void login_ShouldReturnToken_WhenCredentialsValid() {
            UserLoginDto dto = UserLoginDto.builder().email("ivan@test.com").password("pass").build();
            User user = User.builder().email(dto.getEmail()).build();

            when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
            when(jwtService.generateToken(user)).thenReturn("jwt-token");

            AuthenticationResponseDto result = authenticationService.login(dto);

            assertEquals("jwt-token", result.getToken());
            verify(authenticationManager).authenticate(any());
        }
    }

    @Nested
    @DisplayName("Email Verification Tests")
    class VerifyEmailTests {
        @Test
        void verifyEmail_ShouldSucceed_WhenTokenIsValid() {
            User user = User.builder().isEmailVerified(false).build();
            VerificationToken token = VerificationToken.builder()
                    .token("valid-token")
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusHours(1))
                    .build();

            when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

            authenticationService.verifyEmail("valid-token");

            assertTrue(user.isEmailVerified());
            verify(userRepository).save(user);
            verify(tokenRepository).delete(token);
        }

        @Test
        void verifyEmail_ShouldThrow_WhenEmailAlreadyVerified() {
            User user = User.builder().isEmailVerified(true).build();
            VerificationToken token = VerificationToken.builder()
                    .token("valid-token")
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusHours(1))
                    .build();

            when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

            assertThrows(DuplicateEntityException.class, () -> authenticationService.verifyEmail("valid-token"));
        }
    }

    @Nested
    @DisplayName("Password Reset Tests")
    class PasswordTests {
        @Test
        void forgotPassword_ShouldDeleteOldTokenAndCreateNewOne() {
            ForgotPasswordDto dto = ForgotPasswordDto.builder().email("ivan@test.com").build();
            User user = User.builder().id(1L).build();

            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

            authenticationService.forgotPassword(dto);

            verify(passwordResetTokenRepository).deleteByUser(user);
            verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
            verify(eventPublisher).publishEvent(any(OnPasswordResetEvent.class));
        }

        @Test
        void resetPassword_ShouldUpdatePassword_WhenTokenNotExpired() {
            ResetPasswordDto dto = ResetPasswordDto.builder().token("reset-token").newPassword("new123").build();
            User user = User.builder().password("old").build();
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token("reset-token")
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusHours(1))
                    .build();

            when(passwordResetTokenRepository.findByToken("reset-token")).thenReturn(Optional.of(resetToken));
            when(passwordEncoder.encode("new123")).thenReturn("encodedNew");

            authenticationService.resetPassword(dto);

            assertEquals("encodedNew", user.getPassword());
            verify(userRepository).save(user);
            verify(passwordResetTokenRepository).delete(resetToken);
        }
    }
}
