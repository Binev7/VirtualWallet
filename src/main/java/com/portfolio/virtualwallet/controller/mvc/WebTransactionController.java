package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import com.portfolio.virtualwallet.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class WebTransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public String showTransactionHistory(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            Model model,
            @ModelAttribute(MvcConstants.Attributes.CURRENT_USER) User currentUser) {

        if (currentUser == null) return MvcConstants.Views.REDIRECT_LOGIN;

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(AppConstants.EntityFields.CREATED_AT).descending()
        );

        model.addAttribute(MvcConstants.Attributes.TRANSACTIONS_PAGE,
                transactionService.getUserTransactions(currentUser, pageRequest));

        return MvcConstants.Views.TRANSACTIONS_HISTORY;
    }
}