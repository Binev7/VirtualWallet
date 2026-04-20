package com.portfolio.virtualwallet.controller.mvc; // или .advice

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.portfolio.virtualwallet.controller.mvc")
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final UserRepository userRepository;

    @ModelAttribute(MvcConstants.Attributes.CURRENT_USER)
    public User addCurrentUserToModel() {
        try {
            String currentUsername = SecurityUtils.getCurrentUsername();
            if (currentUsername != null && !currentUsername.equals("anonymousUser")) {
                return userRepository.findByUsername(currentUsername).orElse(null);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}