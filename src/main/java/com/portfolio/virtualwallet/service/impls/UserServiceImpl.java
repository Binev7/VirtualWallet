package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.automation.event.OnRegistrationCompleteEvent;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.VerificationToken;
import com.portfolio.virtualwallet.entity.dto.user.ChangePasswordDto;
import com.portfolio.virtualwallet.entity.dto.user.UserDetailsAdminDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import com.portfolio.virtualwallet.exception.*;
import com.portfolio.virtualwallet.mapper.UserMapper;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.VerificationTokenRepository;
import com.portfolio.virtualwallet.repository.specification.UserSpecification;
import com.portfolio.virtualwallet.service.interfaces.UserService;
import com.portfolio.virtualwallet.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.portfolio.virtualwallet.entity.dto.constants.ValidationMessages.Auth.PASSWORDS_DO_NOT_MATCH;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.User.EMAIL_ALREADY_EXISTS;
import static com.portfolio.virtualwallet.exception.ExceptionMessages.User.PHONE_NUMBER_ALREADY_EXISTS;
import static com.portfolio.virtualwallet.utils.AppConstants.User.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository tokenRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Override
    @Transactional(readOnly = true)
    public Page<UserPublicResponseDto> searchPublicUsers(String searchTerm, Pageable pageable) {
        Specification<User> spec = UserSpecification.searchUsers(searchTerm);

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toPublicDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDetailsAdminDto> adminSearchUsers(String searchTerm, Pageable pageable) {
        Specification<User> spec = UserSpecification.searchUsers(searchTerm);

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toAdminDto);
    }

    @Override
    @Transactional
    public void toggleUserBlockStatus(Long userId, boolean isBlocked) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

        if (user.getRole().name().equals(AppConstants.Role.ADMIN)) {
            throw new AdminProtectionException(ExceptionMessages.User.ADMIN_PROTECTION);
        }

        user.setBlocked(isBlocked);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeEmail(User currentUser, String newEmail) {
        if (userRepository.existsByEmail(newEmail)) {
            throw new DuplicateEntityException(EMAIL_ALREADY_EXISTS);
        }

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        user.setEmail(newEmail);
        user.setEmailVerified(false);
        User savedUser = userRepository.save(user);

        String tokenString = UUID.randomUUID().toString();
        VerificationToken verificationToken = userMapper.createVerificationToken(savedUser, tokenString);
        tokenRepository.save(verificationToken);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, appBaseUrl, tokenString));
    }

    @Override
    @Transactional
    public void changePhoneNumber(User currentUser, String newPhoneNumber) {
        if (userRepository.existsByPhoneNumber(newPhoneNumber)) {
            throw new DuplicateEntityException(PHONE_NUMBER_ALREADY_EXISTS);
        }

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        user.setPhoneNumber(newPhoneNumber);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(User currentUser, ChangePasswordDto request) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.User.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException(ExceptionMessages.User.INCORRECT_CURRENT_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidPasswordException(ExceptionMessages.User.PASSWORDS_DO_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}