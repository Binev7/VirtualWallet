package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.dto.auth.AuthenticationResponseDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserLoginDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserRegisterDto;
import com.portfolio.virtualwallet.service.interfaces.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@Valid @RequestBody UserRegisterDto request) {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody UserLoginDto request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }


}
