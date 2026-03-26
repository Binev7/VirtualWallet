package com.portfolio.virtualwallet.service.impls;

import com.portfolio.virtualwallet.service.interfaces.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static com.portfolio.virtualwallet.utils.AppConstants.Email.*;
import static com.portfolio.virtualwallet.utils.AppConstants.Logging.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    @Override
    public void sendVerificationEmail(String to, String confirmationUrl) {
        try {
            Context context = new Context();
            context.setVariable(URL_VARIABLE, confirmationUrl);

            String htmlContent = templateEngine.process(VERIFICATION_TEMPLATE, context);

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(VERIFICATION_SUBJECT);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info(EMAIL_SEND_SUCCESS, to);

        } catch (Exception e) {
            log.error(EMAIL_SEND_ERROR, to, e);
        }
    }
}