package com.portfolio.virtualwallet.service.interfaces;

import com.portfolio.virtualwallet.entity.dto.auth.AuthenticationResponseDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserLoginDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserRegisterDto;

public interface AuthenticationService {

    AuthenticationResponseDto register(UserRegisterDto request);

    AuthenticationResponseDto login(UserLoginDto request);
}
