package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.user.ChangeEmailDto;
import com.portfolio.virtualwallet.entity.dto.user.ChangePasswordDto;
import com.portfolio.virtualwallet.entity.dto.user.ChangePhoneNumberDto;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.ExceptionMessages;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.service.interfaces.UserService;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(MvcConstants.Paths.PROFILE)
@RequiredArgsConstructor
public class WebUserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public String showProfilePage(Model model) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.User.USER_NOT_FOUND));

        model.addAttribute(MvcConstants.Attributes.CURRENT_USER, currentUser);

        if (!model.containsAttribute(MvcConstants.Attributes.CHANGE_EMAIL_REQUEST)) {
            model.addAttribute(MvcConstants.Attributes.CHANGE_EMAIL_REQUEST, new ChangeEmailDto());
        }

        if (!model.containsAttribute(MvcConstants.Attributes.CHANGE_PHONE_REQUEST)) {
            model.addAttribute(MvcConstants.Attributes.CHANGE_PHONE_REQUEST, new ChangePhoneNumberDto());
        }

        if (!model.containsAttribute(MvcConstants.Attributes.CHANGE_PASSWORD_REQUEST)) {
            model.addAttribute(MvcConstants.Attributes.CHANGE_PASSWORD_REQUEST, new ChangePasswordDto());
        }

        return MvcConstants.Views.PROFILE;
    }

    @PostMapping("/change-email")
    public String changeEmail(@Valid @ModelAttribute(MvcConstants.Attributes.CHANGE_EMAIL_REQUEST) ChangeEmailDto dto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    MvcConstants.getBindingResultKey(MvcConstants.Attributes.CHANGE_EMAIL_REQUEST),
                    bindingResult
            );
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.CHANGE_EMAIL_REQUEST, dto);
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, MvcConstants.Messages.INVALID_EMAIL_FORMAT);

            return MvcConstants.Views.REDIRECT_PROFILE;
        }

        try {
            User currentUser = getCurrentUser();
            userService.changeEmail(currentUser, dto.getNewEmail());
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.EMAIL_UPDATED_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
        }

        return MvcConstants.Views.REDIRECT_PROFILE;
    }

    @PostMapping("/change-phone")
    public String changePhoneNumber(@Valid @ModelAttribute(MvcConstants.Attributes.CHANGE_PHONE_REQUEST) ChangePhoneNumberDto dto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    MvcConstants.getBindingResultKey(MvcConstants.Attributes.CHANGE_PHONE_REQUEST),
                    bindingResult
            );
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.CHANGE_PHONE_REQUEST, dto);
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, MvcConstants.Messages.INVALID_PHONE_FORMAT);

            return MvcConstants.Views.REDIRECT_PROFILE;
        }

        try {
            User currentUser = getCurrentUser();
            userService.changePhoneNumber(currentUser, dto.getNewPhoneNumber());
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.PHONE_UPDATED_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
        }

        return MvcConstants.Views.REDIRECT_PROFILE;
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute(MvcConstants.Attributes.CHANGE_PASSWORD_REQUEST) ChangePasswordDto dto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    MvcConstants.getBindingResultKey(MvcConstants.Attributes.CHANGE_PASSWORD_REQUEST),
                    bindingResult
            );
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.CHANGE_PASSWORD_REQUEST, dto);
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, MvcConstants.Messages.INVALID_PASSWORD_FORMAT);

            return MvcConstants.Views.REDIRECT_PROFILE;
        }

        try {
            User currentUser = getCurrentUser();
            userService.changePassword(currentUser, dto);
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.PASSWORD_UPDATED_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
        }

        return MvcConstants.Views.REDIRECT_PROFILE;
    }

    private User getCurrentUser() {
        return userRepository.findByUsername(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.User.USER_NOT_FOUND));
    }
}