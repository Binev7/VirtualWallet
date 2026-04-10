package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.OtpVerificationRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransferRequestDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.ExceptionMessages;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import com.portfolio.virtualwallet.service.interfaces.UserService;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class WebTransferController {

    private final TransactionService transactionService;
    private final WalletService walletService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public String showSearchPage(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isBlank()) {
            Page<UserPublicResponseDto> users = userService.searchPublicUsers(query, PageRequest.of(0, 10));
            model.addAttribute("searchResults", users.getContent());
            model.addAttribute("query", query);
        }
        return MvcConstants.Views.TRANSFER_SEARCH;
    }

    @GetMapping("/new")
    public String showTransferForm(@RequestParam String receiverUsername, Model model) {
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.User.USER_NOT_FOUND));

        Long receiverWalletId = walletService.getUserWallets(receiver).get(0).getId();

        model.addAttribute(MvcConstants.Attributes.WALLETS, walletService.getMyWallets());

        TransferRequestDto request = new TransferRequestDto();
        request.setReceiverWalletId(receiverWalletId);

        model.addAttribute(MvcConstants.Attributes.TRANSFER_REQUEST, request);
        model.addAttribute(MvcConstants.Attributes.RECEIVER_NAME, receiverUsername);

        return MvcConstants.Views.TRANSFER_FORM;
    }

    @PostMapping("/process")
    public String handleTransfer(
            @ModelAttribute(MvcConstants.Attributes.TRANSFER_REQUEST) TransferRequestDto request,
            @RequestParam String receiverName,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            String currentUsername = SecurityUtils.getCurrentUsername();
            User currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.User.USER_NOT_FOUND));

            TransactionResponseDto response = transactionService.transfer(currentUser, request);

            if (request.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
                redirectAttributes.addFlashAttribute(MvcConstants.Attributes.TRANSACTION_ID, response.getTransactionId());
                redirectAttributes.addFlashAttribute("infoMessage", MvcConstants.Messages.OTP_REQUIRED);
                return "redirect:/transfer/otp";
            }

            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.TRANSFER_SUCCESS);
            return MvcConstants.Views.REDIRECT_HOME;

        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
            model.addAttribute(MvcConstants.Attributes.WALLETS, walletService.getMyWallets());
            model.addAttribute(MvcConstants.Attributes.RECEIVER_NAME, receiverName);
            return MvcConstants.Views.TRANSFER_FORM;
        }
    }

    @GetMapping("/otp")
    public String showOtpPage(Model model) {
        if (!model.containsAttribute(MvcConstants.Attributes.TRANSACTION_ID)) {
            return "redirect:/transfer";
        }

        Long transactionId = (Long) model.getAttribute(MvcConstants.Attributes.TRANSACTION_ID);

        OtpVerificationRequestDto otpRequest = new OtpVerificationRequestDto();
        otpRequest.setTransactionId(transactionId);

        model.addAttribute(MvcConstants.Attributes.OTP_REQUEST, otpRequest);
        return MvcConstants.Views.TRANSFER_OTP;
    }

    @PostMapping("/otp/verify")
    public String verifyOtp(
            @ModelAttribute(MvcConstants.Attributes.OTP_REQUEST) OtpVerificationRequestDto request,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            String currentUsername = SecurityUtils.getCurrentUsername();
            User currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.User.USER_NOT_FOUND));

            transactionService.verifyOtp(currentUser, request);

            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.TRANSFER_SUCCESS);
            return MvcConstants.Views.REDIRECT_HOME;

        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
            model.addAttribute(MvcConstants.Attributes.TRANSACTION_ID, request.getTransactionId());
            return MvcConstants.Views.TRANSFER_OTP;
        }
    }
}