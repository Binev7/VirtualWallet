package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.dto.auth.*;

public interface AuthenticationService {

    AuthenticationResponseDto register(UserRegisterDto request);

    AuthenticationResponseDto login(UserLoginDto request);

    void verifyEmail(String token);

    void forgotPassword(ForgotPasswordDto request);

    void resetPassword(ResetPasswordDto request);
}
