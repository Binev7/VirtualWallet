package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.enums.TransactionStatus;
import com.portfolio.virtualwallet.entity.enums.TransactionType;
import com.portfolio.virtualwallet.service.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.portfolio.virtualwallet.utils.AppConstants.EntityFields.CREATED_AT;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;

@Controller
@RequestMapping(MvcConstants.Paths.ADMIN_TRANSACTIONS)
@RequiredArgsConstructor
public class WebAdminTransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public String showAllTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = CREATED_AT) String sortBy,
            Model model,
            @ModelAttribute(MvcConstants.Attributes.CURRENT_USER) User currentUser) {

        if (currentUser == null) return MvcConstants.Views.REDIRECT_LOGIN;

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        model.addAttribute(MvcConstants.Attributes.TRANSACTIONS_PAGE,
                transactionService.getGlobalTransactionsForAdmin(
                        startDateTime, endDateTime, username, direction, type, status, page, size, sortBy));

        model.addAttribute(MvcConstants.Attributes.START_DATE, startDate);
        model.addAttribute(MvcConstants.Attributes.END_DATE, endDate);
        model.addAttribute(MvcConstants.Attributes.QUERY, username);
        model.addAttribute(MvcConstants.Attributes.SELECTED_TYPE, type);
        model.addAttribute(MvcConstants.Attributes.SELECTED_STATUS, status);

        return MvcConstants.Views.ADMIN_TRANSACTIONS_LIST;
    }
}