package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.dto.auth.*;
import com.portfolio.virtualwallet.service.interfaces.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.*;

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

    @GetMapping("/verifyEmail")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam("token") String token) {
        authenticationService.verifyEmail(token);

        return ResponseEntity.ok(Map.of("message", EMAIL_VERIFIED));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordDto request) {
        authenticationService.forgotPassword(request);

        return ResponseEntity.ok(Map.of("message", FORGOT_PASSWORD_SUCCESS));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordDto request) {
        authenticationService.resetPassword(request);

        return ResponseEntity.ok(Map.of("message", RESET_PASSWORD_SUCCESS));
    }
}
