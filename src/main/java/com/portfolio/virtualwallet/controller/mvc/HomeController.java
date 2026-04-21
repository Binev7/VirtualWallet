package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import com.portfolio.virtualwallet.entity.dto.transaction.TransactionHistoryDto;
import com.portfolio.virtualwallet.entity.dto.wallet.WalletResponseDto;
import com.portfolio.virtualwallet.service.interfaces.CardService;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final WalletService walletService;
    private final CardService cardService;
    private final TransactionService transactionService;

    @GetMapping("/")
    public String showIndexPage(
            Model model,
            @ModelAttribute(MvcConstants.Attributes.CURRENT_USER) User currentUser) {

        if (currentUser == null) {
            return MvcConstants.Views.LANDING_PAGE;
        }

        List<WalletResponseDto> wallets = walletService.getMyWallets();
        List<CardResponseDto> cards = cardService.getAllMyCards();

        model.addAttribute(MvcConstants.Attributes.WALLETS, wallets);
        model.addAttribute(MvcConstants.Attributes.CARDS, cards);

        if (!wallets.isEmpty()) {
            model.addAttribute(MvcConstants.Attributes.WALLET, wallets.get(0));
        }

        if (!cards.isEmpty()) {
            model.addAttribute(MvcConstants.Attributes.CARD, cards.get(0));
        }

        List<List<TransactionHistoryDto>> allRecentTransactions = new ArrayList<>();

        for (WalletResponseDto wallet : wallets) {
            Page<TransactionHistoryDto> transactionsPage = transactionService.getWalletHistory(
                    currentUser,
                    wallet.getId(),
                    null, null, null, null,
                    0, 5
            );
            allRecentTransactions.add(transactionsPage.getContent());
        }

        model.addAttribute(MvcConstants.Attributes.ALL_RECENT_TRANSACTIONS, allRecentTransactions);

        return MvcConstants.Views.DASHBOARD_INDEX;
    }
}