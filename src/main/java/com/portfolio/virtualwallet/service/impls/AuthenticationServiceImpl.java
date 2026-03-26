package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.automation.event.OnRegistrationCompleteEvent;
import com.portfolio.virtualwallet.entity.VerificationToken;
import com.portfolio.virtualwallet.entity.dto.auth.AuthenticationResponseDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserLoginDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserRegisterDto;
import com.portfolio.virtualwallet.entity.Role;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.ExceptionMessages;
import com.portfolio.virtualwallet.exception.TokenExpiredException;
import com.portfolio.virtualwallet.mapper.UserMapper;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.VerificationTokenRepository;
import com.portfolio.virtualwallet.security.JwtService;
import com.portfolio.virtualwallet.service.interfaces.AuthenticationService;
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

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Override
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

        userRepository.save(user);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appBaseUrl));

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDto(jwtToken);
    }

    @Override
    public AuthenticationResponseDto login(UserLoginDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, request.getEmail())));

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
    }
}