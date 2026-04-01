package com.portfolio.virtualwallet.automation.event;

import com.portfolio.virtualwallet.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnPasswordResetEvent extends ApplicationEvent {
    private final User user;
    private final String appBaseUrl;
    private final String token;

    public OnPasswordResetEvent(User user, String appBaseUrl, String token) {
        super(user);
        this.user = user;
        this.appBaseUrl = appBaseUrl;
        this.token = token;
    }
}