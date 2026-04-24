package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.transaction.DepositRequestDto;
import com.portfolio.virtualwallet.entity.dto.transaction.WithdrawalRequestDto;

import com.portfolio.virtualwallet.service.interfaces.CardService;
import com.portfolio.virtualwallet.service.interfaces.DepositService;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import com.portfolio.virtualwallet.service.interfaces.WithdrawalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class WebFundingController {

    private final DepositService depositService;
    private final CardService cardService;
    private final WalletService walletService;
    private final WithdrawalService withdrawalService;

    @GetMapping("/deposit")
    public String showTopUpPage(Model model) {
        model.addAttribute(MvcConstants.Attributes.CARDS, cardService.getAllMyCards());
        model.addAttribute(MvcConstants.Attributes.WALLETS, walletService.getMyWallets());

        model.addAttribute(MvcConstants.Attributes.DEPOSIT_REQUEST, new DepositRequestDto());

        return MvcConstants.Views.TOP_UP;
    }

    @PostMapping("/deposit")
    public String handleDeposit(
            @ModelAttribute(MvcConstants.Attributes.DEPOSIT_REQUEST) DepositRequestDto request,
            RedirectAttributes redirectAttributes,
            Model model,
            @AuthenticationPrincipal User currentUser) {
        try {
            depositService.depositToWallet(currentUser, request);

            redirectAttributes.addFlashAttribute(
                    MvcConstants.Attributes.SUCCESS_MESSAGE,
                    MvcConstants.Messages.TOP_UP_SUCCESS
            );

            return MvcConstants.Views.REDIRECT_HOME;
        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
            model.addAttribute(MvcConstants.Attributes.CARDS, cardService.getAllMyCards());
            model.addAttribute(MvcConstants.Attributes.WALLETS, walletService.getMyWallets());
            return MvcConstants.Views.TOP_UP;
        }
    }

    @GetMapping("/withdraw")
    public String showWithdrawPage(Model model) {
        model.addAttribute(MvcConstants.Attributes.CARDS, cardService.getAllMyCards());
        model.addAttribute(MvcConstants.Attributes.WALLETS, walletService.getMyWallets());

        model.addAttribute(MvcConstants.Attributes.WITHDRAWAL_REQUEST, new WithdrawalRequestDto());

        return MvcConstants.Views.WITHDRAW;
    }

    @PostMapping("/withdraw")
    public String handleWithdraw(
            @ModelAttribute(MvcConstants.Attributes.WITHDRAWAL_REQUEST) WithdrawalRequestDto request,
            RedirectAttributes redirectAttributes,
            Model model,
            @AuthenticationPrincipal User currentUser) {
        try {
            withdrawalService.withdrawFromWallet(currentUser, request);

            redirectAttributes.addFlashAttribute(
                    MvcConstants.Attributes.SUCCESS_MESSAGE,
                    MvcConstants.Messages.WITHDRAW_SUCCESS
            );

            return MvcConstants.Views.REDIRECT_HOME;
        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
            model.addAttribute(MvcConstants.Attributes.CARDS, cardService.getAllMyCards());
            model.addAttribute(MvcConstants.Attributes.WALLETS, walletService.getMyWallets());
            return MvcConstants.Views.WITHDRAW;
        }
    }
}