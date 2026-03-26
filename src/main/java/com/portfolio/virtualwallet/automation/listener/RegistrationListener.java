package com.portfolio.virtualwallet.automation.listener;

import com.portfolio.virtualwallet.automation.event.OnRegistrationCompleteEvent;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.VerificationToken;
import com.portfolio.virtualwallet.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.portfolio.virtualwallet.utils.AppConstants.EmailVerification.VERIFY_EMAIL_ENDPOINT;
import static com.portfolio.virtualwallet.utils.AppConstants.Logging.REGISTRATION_SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationListener {

    private final VerificationTokenRepository tokenRepository;

    @EventListener
    public void handleRegistrationComplete(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(VerificationToken.EXPIRATION_HOURS))
                .build();

        tokenRepository.save(verificationToken);

        String confirmationUrl = event.getAppUrl() + VERIFY_EMAIL_ENDPOINT + token;

        log.info(REGISTRATION_SUCCESS, user.getEmail(), confirmationUrl);
    }
}