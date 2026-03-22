package com.portfolio.virtualwallet.utils;

import com.portfolio.virtualwallet.exception.UnauthorizedException;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.portfolio.virtualwallet.exception.ExceptionMessages.Security.*;

@UtilityClass
public class SecurityUtils {

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            return authentication.getName();
        }

        throw new UnauthorizedException(UNAUTHENTICATED);
    }
}