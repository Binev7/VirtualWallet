package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.exception.ExceptionMessages;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import com.portfolio.virtualwallet.utils.AppConstants; // ИМПОРТИРАЙ ТОВА
import com.portfolio.virtualwallet.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;

@Controller
@RequestMapping(MvcConstants.Paths.TRANSACTIONS)
@RequiredArgsConstructor
public class WebTransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @GetMapping
    public String showTransactionHistory(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            Model model) {

        User currentUser = userRepository.findByUsername(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.User.USER_NOT_FOUND));

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