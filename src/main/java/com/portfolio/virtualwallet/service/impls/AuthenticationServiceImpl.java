package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.entity.dto.auth.AuthenticationResponseDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserLoginDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserRegisterDto;
import com.portfolio.virtualwallet.entity.Role;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.exception.DuplicateEntityException;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.mapper.UserMapper;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.security.JwtService;
import com.portfolio.virtualwallet.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
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

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDto(jwtToken);
    }

    @Override
    public AuthenticationResponseDto login(UserLoginDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, request.getUsername())));

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDto(jwtToken);
    }
}