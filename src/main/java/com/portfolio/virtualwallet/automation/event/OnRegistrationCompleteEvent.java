package com.portfolio.virtualwallet.automation.event;

import com.portfolio.virtualwallet.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OnRegistrationCompleteEvent {
    private final User user;
    private final String appUrl;
    private final String token;
}