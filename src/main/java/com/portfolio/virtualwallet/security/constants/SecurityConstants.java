package com.portfolio.virtualwallet.security.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityConstants {

    public final String AUTHORIZATION_HEADER = "Authorization";
    public final String BEARER_PREFIX = "Bearer ";
    public final int BEARER_PREFIX_LENGTH = 7;
    public final String ROLE_PREFIX = "ROLE_";

    public final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/error"
    };
}