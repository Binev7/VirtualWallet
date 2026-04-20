package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.RecurringTransactionRequestDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.ExceptionMessages;
import com.portfolio.virtualwallet.mapper.UserMapper;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.repository.specification.UserSpecification;
import com.portfolio.virtualwallet.service.interfaces.RecurringTransactionService;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/recurring")
@RequiredArgsConstructor
public class WebRecurringController {

    private final RecurringTransactionService recurringService;
    private final WalletService walletService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("/new")
    public String showRecurringForm(@RequestParam String receiverUsername, Model model) {

        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.User.USER_NOT_FOUND));

        Long receiverWalletId = walletService.getUserWallets(receiver).get(0).getId();

        model.addAttribute(MvcConstants.Attributes.WALLETS, walletService.getMyWallets());

        RecurringTransactionRequestDto request = new RecurringTransactionRequestDto();
        request.setReceiverWalletId(receiverWalletId);

        model.addAttribute(MvcConstants.Attributes.RECURRING_REQUEST, request);
        model.addAttribute(MvcConstants.Attributes.RECEIVER_NAME, receiverUsername);

        return MvcConstants.Views.RECURRING_FORM;
    }

    @GetMapping
    public String showMyRecurringTransactions(Model model, User currentUser) {
        model.addAttribute(MvcConstants.Attributes.RECURRING_TRANSACTIONS,
                recurringService.getUserRecurringTransfers(currentUser));

        return MvcConstants.Views.RECURRING_LIST;
    }

    @GetMapping("/search")
    public String showRecurringSearch(
            @RequestParam(required = false) String query,
            Model model,
            @ModelAttribute(MvcConstants.Attributes.CURRENT_USER) User currentUser) {

        if (currentUser == null) return MvcConstants.Views.REDIRECT_LOGIN;

        if (query != null && !query.trim().isEmpty()) {
            Specification<User> spec = UserSpecification.searchUsers(query, currentUser.getUsername());
            List<User> foundUsers = userRepository.findAll(spec);

            List<UserPublicResponseDto> publicUsers = foundUsers.stream()
                    .map(userMapper::toPublicDto)
                    .toList();

            model.addAttribute(MvcConstants.Attributes.SEARCH_RESULTS, publicUsers);
        }

        model.addAttribute(MvcConstants.Attributes.QUERY, query);
        return MvcConstants.Views.RECURRING_SEARCH;
    }

    @PostMapping("/process")
    public String processRecurringTransaction(
            @RequestParam String receiverName,
            @Valid @ModelAttribute(MvcConstants.Attributes.RECURRING_REQUEST) RecurringTransactionRequestDto request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @ModelAttribute(MvcConstants.Attributes.CURRENT_USER) User currentUser) {

        if (currentUser == null) return MvcConstants.Views.REDIRECT_LOGIN;

        if (bindingResult.hasErrors()) {
            model.addAttribute(MvcConstants.Attributes.WALLETS, walletService.getMyWallets());
            model.addAttribute(MvcConstants.Attributes.RECEIVER_NAME, receiverName);
            return MvcConstants.Views.RECURRING_FORM;
        }

        recurringService.createRecurringTransfer(currentUser, request);

        redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.RECURRING_SETUP_SUCCESS);
        return MvcConstants.Views.REDIRECT_HOME;
    }

    @PostMapping("/cancel/{id}")
    public String cancelRecurring(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            @ModelAttribute(MvcConstants.Attributes.CURRENT_USER) User currentUser) {

        if (currentUser == null) return MvcConstants.Views.REDIRECT_LOGIN;

        try {
            recurringService.cancelRecurringTransfer(currentUser, id);
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE,
                    MvcConstants.Messages.RECURRING_CANCELLED_SUCCESS);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
        }

        return MvcConstants.Views.REDIRECT_RECURRING;
    }
}