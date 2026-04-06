package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionHistoryDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletResponseDto;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.ExceptionMessages;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.service.interfaces.CardService;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final CardService cardService;
    private final TransactionService transactionService;

    @GetMapping("/")
    public String showIndexPage(Model model) {

        String currentUsername = SecurityUtils.getCurrentUsername();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.User.USER_NOT_FOUND));

        model.addAttribute(MvcConstants.Attributes.CURRENT_USER, currentUser);

        List<WalletResponseDto> wallets = walletService.getMyWallets();
        WalletResponseDto primaryWallet = null;

        if (!wallets.isEmpty()) {
            primaryWallet = wallets.get(0);
            model.addAttribute(MvcConstants.Attributes.WALLET, primaryWallet);
        }

        List<CardResponseDto> cards = cardService.getAllMyCards();
        if (!cards.isEmpty()) {
            model.addAttribute(MvcConstants.Attributes.CARD, cards.get(0));
        }

        List<TransactionHistoryDto> recentTransactions = Collections.emptyList();

        if (primaryWallet != null) {
            Page<TransactionHistoryDto> transactionsPage = transactionService.getWalletHistory(
                    currentUser,
                    primaryWallet.getId(),
                    null,
                    null,
                    null,
                    null,
                    0,
                    5
            );

            recentTransactions = transactionsPage.getContent();
        }

        model.addAttribute(MvcConstants.Attributes.RECENT_TRANSACTIONS, recentTransactions);

        return MvcConstants.Views.DASHBOARD_INDEX;
    }
}