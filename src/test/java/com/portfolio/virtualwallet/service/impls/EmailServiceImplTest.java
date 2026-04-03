package com.portfolio.virtualwallet.service.impls;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "senderEmail", "test@test.com");
    }

    @Test
    void sendHtmlEmail_ShouldSucceed_WhenParametersAreValid() {
        String to = "user@example.com";
        String subject = "Test Subject";
        String templateName = "template";
        Map<String, Object> variables = new HashMap<>();
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(templateEngine.process(eq(templateName), any(Context.class))).thenReturn("<html></html>");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendHtmlEmail(to, subject, templateName, variables);

        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendHtmlEmail_ShouldCatchAndLogError_WhenExceptionOccurs() {
        String to = "user@example.com";
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Mail server down"));

        emailService.sendHtmlEmail(to, "Subject", "template", new HashMap<>());

        verify(mailSender, never()).send(any(MimeMessage.class));
    }
}
