package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.Card;
import com.portfolio.virtualwallet.entity.Transaction;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.Wallet;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showIndexPage(Model model) {
        User dummyUser = User.builder().username("Admiral Sky").build();
        model.addAttribute("currentUser", dummyUser);

        Wallet dummyWallet = Wallet.builder().balance(new BigDecimal("321.50")).build();
        model.addAttribute("wallet", dummyWallet);

        Card dummyCard = Card.builder().cardNumber("4000123456784321").build();
        model.addAttribute("card", dummyCard);

        Transaction tx1 = Transaction.builder()
                .amount(new BigDecimal("150.00"))
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();

        Transaction tx2 = Transaction.builder()
                .amount(new BigDecimal("15.00"))
                .type(TransactionType.WITHDRAW)
                .status(TransactionStatus.COMPLETED)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        model.addAttribute("recentTransactions", List.of(tx1, tx2));

        return "dashboard/index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }
}