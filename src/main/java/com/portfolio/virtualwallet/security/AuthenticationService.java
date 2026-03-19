package com.portfolio.virtualwallet.security;

import com.portfolio.virtualwallet.entity.dto.auth.AuthenticationResponseDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserRegisterDto;
import com.portfolio.virtualwallet.entity.Role;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponseDto register(UserRegisterDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setBlocked(false);

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDto(jwtToken);
    }

    public AuthenticationResponseDto authenticate(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDto(jwtToken);
    }
}