package com.portfolio.virtualwallet.automation.listener;

import com.portfolio.virtualwallet.automation.event.*;
import com.portfolio.virtualwallet.entity.RecurringTransaction;
import com.portfolio.virtualwallet.entity.Transaction;
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
        Transaction tx = event.getOtp().getTransaction();

        String email = event.getUserEmail();
        String otpCode = event.getOtp().getOtpCode();

        Map<String, Object> vars = new HashMap<>();
        vars.put(OTP_VARIABLE, otpCode);
        vars.put(AMOUNT_VARIABLE, tx.getAmount());

        emailService.sendHtmlEmail(email, OTP_SUBJECT, OTP_TEMPLATE, vars);

        log.info(EMAIL_SEND_SUCCESS, email);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTransactionSuccess(OnTransactionSuccessEvent event) {
        Transaction tx = event.getTransaction();

        Map<String, Object> senderVars = new HashMap<>();
        senderVars.put(AMOUNT_VARIABLE, tx.getAmount());
        senderVars.put(RECIPIENT_VARIABLE, tx.getReceiverWallet().getOwner().getUsername());

        emailService.sendHtmlEmail(
                tx.getSenderWallet().getOwner().getEmail(),
                TRANSACTION_SUCCESS_SUBJECT,
                TRANSACTION_SUCCESS_TEMPLATE,
                senderVars
        );

        Map<String, Object> receiverVars = new HashMap<>();
        receiverVars.put(AMOUNT_VARIABLE, tx.getAmount());
        receiverVars.put(SENDER_VARIABLE, tx.getSenderWallet().getOwner().getUsername());

        emailService.sendHtmlEmail(
                tx.getReceiverWallet().getOwner().getEmail(),
                RECEIVED_MONEY_SUBJECT,
                RECEIVED_MONEY_TEMPLATE,
                receiverVars
        );
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRecurringTransactionFailure(OnRecurringTransactionFailedEvent event) {
        RecurringTransaction rTx = event.getRecurringTransaction();
        String email = rTx.getSenderWallet().getOwner().getEmail();

        Map<String, Object> vars = new HashMap<>();
        vars.put(AMOUNT_VARIABLE, rTx.getAmount());
        vars.put(RECIPIENT_VARIABLE, rTx.getReceiverWallet().getOwner().getUsername());
        vars.put(REASON_VARIABLE, event.getFailureReason());

        emailService.sendHtmlEmail(email, RECURRING_FAILED_SUBJECT, RECURRING_FAILED_TEMPLATE, vars);

        log.info(EMAIL_SEND_SUCCESS, email);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePasswordResetEvent(OnPasswordResetEvent event) {
        String email = event.getUser().getEmail();

        Map<String, Object> vars = new HashMap<>();
        vars.put(RESET_TOKEN_VARIABLE, event.getToken());
        vars.put(USERNAME_VARIABLE, event.getUser().getUsername());

        emailService.sendHtmlEmail(email, PASSWORD_RESET_SUBJECT, PASSWORD_RESET_TEMPLATE, vars);

        log.info(EMAIL_SEND_SUCCESS, email);
    }
}