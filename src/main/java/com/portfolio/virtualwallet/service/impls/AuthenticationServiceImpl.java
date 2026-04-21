package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.automation.event.OnPasswordResetEvent;
import com.portfolio.virtualwallet.automation.event.OnRegistrationCompleteEvent;
import com.portfolio.virtualwallet.entity.PasswordResetToken;
import com.portfolio.virtualwallet.entity.VerificationToken;
import com.portfolio.virtualwallet.entity.dto.auth.*;
import com.portfolio.virtualwallet.entity.enums.Role;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.exception.*;
import com.portfolio.virtualwallet.mapper.UserMapper;
import com.portfolio.virtualwallet.repository.PasswordResetTokenRepository;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.VerificationTokenRepository;
import com.portfolio.virtualwallet.security.JwtService;
import com.portfolio.virtualwallet.service.interfaces.AuthenticationService;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.User.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final WalletService walletService;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Override
    @Transactional
    public AuthenticationResponseDto register(UserRegisterDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateEntityException(USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEntityException(EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateEntityException(PHONE_NUMBER_ALREADY_EXISTS);
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setBlocked(false);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        String tokenString = java.util.UUID.randomUUID().toString();
        VerificationToken verificationToken = userMapper.createVerificationToken(savedUser, tokenString);
        tokenRepository.save(verificationToken);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, appBaseUrl, tokenString));

        String jwtToken = jwtService.generateToken(savedUser);
        return new AuthenticationResponseDto(jwtToken);
    }

    @Override
    public AuthenticationResponseDto login(UserLoginDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, request.getEmail())));

        if (user.isBlocked()) {
            throw new UserBlockedException(USER_BLOCKED);
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDto(jwtToken);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Token.INVALID_TOKEN));

        if (verificationToken.isExpired()) {
            tokenRepository.delete(verificationToken);
            throw new TokenExpiredException(ExceptionMessages.Token.EXPIRED_TOKEN);
        }

        User user = verificationToken.getUser();

        if (user.isEmailVerified()) {
            throw new DuplicateEntityException(ExceptionMessages.Verification.EMAIL_ALREADY_VERIFIED);
        }

        user.setEmailVerified(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);
        walletService.initializeDefaultWallet(user);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, request.getEmail())));

        passwordResetTokenRepository.deleteByUser(user);

        String tokenString = java.util.UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(tokenString, user);
        passwordResetTokenRepository.save(resetToken);

        eventPublisher.publishEvent(new OnPasswordResetEvent(user, appBaseUrl, tokenString));
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDto request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Token.INVALID_TOKEN));

        if (resetToken.isExpired()) {
            passwordResetTokenRepository.delete(resetToken);
            throw new TokenExpiredException(ExceptionMessages.Token.EXPIRED_TOKEN);
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}