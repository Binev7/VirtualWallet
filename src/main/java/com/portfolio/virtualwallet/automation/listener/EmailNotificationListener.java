package com.portfolio.virtualwallet.automation.listener;

import com.portfolio.virtualwallet.automation.event.OnLargeTransactionEvent;
import com.portfolio.virtualwallet.automation.event.OnRegistrationCompleteEvent;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.portfolio.virtualwallet.utils.AppConstants.Email.*;
import static com.portfolio.virtualwallet.utils.AppConstants.EmailVerification.VERIFY_EMAIL_ENDPOINT;
import static com.portfolio.virtualwallet.utils.AppConstants.Logging.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationListener {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRegistrationComplete(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String confirmationUrl = event.getAppUrl() + VERIFY_EMAIL_ENDPOINT + event.getToken();

        Map<String, Object> vars = new HashMap<>();
        vars.put(URL_VARIABLE, confirmationUrl);

        emailService.sendHtmlEmail(user.getEmail(), VERIFICATION_SUBJECT, VERIFICATION_TEMPLATE, vars);

        log.info(EMAIL_SEND_SUCCESS, user.getEmail());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLargeTransaction(OnLargeTransactionEvent event) {
        String email = event.getUserEmail();
        String otpCode = event.getOtp().getOtpCode();

        Map<String, Object> vars = new HashMap<>();
        vars.put(OTP_VARIABLE, otpCode);

        emailService.sendHtmlEmail(email, OTP_SUBJECT, OTP_TEMPLATE, vars);

        log.info(EMAIL_SEND_SUCCESS, email);
    }
}