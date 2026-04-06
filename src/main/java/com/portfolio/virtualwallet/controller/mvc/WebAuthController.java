package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.dto.auth.ForgotPasswordDto;
import com.portfolio.virtualwallet.entity.dto.auth.ResetPasswordDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserLoginDto;
import com.portfolio.virtualwallet.entity.dto.auth.UserRegisterDto;
import com.portfolio.virtualwallet.service.interfaces.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class WebAuthController {

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public String showLoginPage() {
        return MvcConstants.Views.LOGIN;
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response,
            Model model) {

        try {
            UserLoginDto loginDto = new UserLoginDto(username, password);
            String token = authenticationService.login(loginDto).getToken();

            ResponseCookie jwtCookie = ResponseCookie.from(MvcConstants.Cookies.JWT_COOKIE_NAME, token)
                    .httpOnly(true)
                    .secure(false)
                    .path(MvcConstants.Cookies.PATH)
                    .maxAge(MvcConstants.Cookies.MAX_AGE_SECONDS)
                    .sameSite(MvcConstants.Cookies.SAME_SITE_STRICT)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

            return MvcConstants.Views.REDIRECT_HOME;

        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, true);
            return MvcConstants.Views.LOGIN;
        }
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return MvcConstants.Views.REGISTER;
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String password,
            Model model) {
        try {
            UserRegisterDto registerDto = UserRegisterDto.builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .phoneNumber(phoneNumber)
                    .build();

            authenticationService.register(registerDto);

            model.addAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.REGISTRATION_SUCCESS);
            return MvcConstants.Views.LOGIN;
        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
            return MvcConstants.Views.REGISTER;
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return MvcConstants.Views.FORGOT_PASSWORD;
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email, Model model) {
        try {
            ForgotPasswordDto dto = new ForgotPasswordDto(email);
            authenticationService.forgotPassword(dto);
            model.addAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.RESET_LINK_SENT);
        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, MvcConstants.Messages.USER_NOT_FOUND);
        }
        return MvcConstants.Views.FORGOT_PASSWORD;
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute(MvcConstants.Attributes.TOKEN, token);
        return MvcConstants.Views.RESET_PASSWORD;
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {
        try {
            ResetPasswordDto dto = ResetPasswordDto.builder()
                    .token(token)
                    .newPassword(newPassword)
                    .confirmPassword(confirmPassword)
                    .build();

            authenticationService.resetPassword(dto);
            model.addAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.RESET_SUCCESS);
            return MvcConstants.Views.LOGIN;
        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, MvcConstants.Messages.INVALID_TOKEN);
            return MvcConstants.Views.RESET_PASSWORD;
        }
    }
}