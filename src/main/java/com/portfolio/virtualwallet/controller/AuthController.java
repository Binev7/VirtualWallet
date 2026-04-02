package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.dto.auth.*;
import com.portfolio.virtualwallet.service.interfaces.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.portfolio.virtualwallet.controller.docs.SwaggerMessages.Auth.*;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = REGISTER_SUMMARY, description = REGISTER_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = CREATED_201),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "409", description = CONFLICT_409, content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@Valid @RequestBody UserRegisterDto request) {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @Operation(summary = LOGIN_SUMMARY, description = LOGIN_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED_401, content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody UserLoginDto request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @Operation(summary = VERIFY_EMAIL_SUMMARY, description = VERIFY_EMAIL_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @GetMapping("/verifyEmail")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam("token") String token) {
        authenticationService.verifyEmail(token);
        return ResponseEntity.ok(Map.of("message", EMAIL_VERIFIED));
    }

    @Operation(summary = FORGOT_PASSWORD_SUMMARY, description = FORGOT_PASSWORD_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordDto request) {
        authenticationService.forgotPassword(request);
        return ResponseEntity.ok(Map.of("message", FORGOT_PASSWORD_SUCCESS));
    }

    @Operation(summary = RESET_PASSWORD_SUMMARY, description = RESET_PASSWORD_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordDto request) {
        authenticationService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message", RESET_PASSWORD_SUCCESS));
    }
}