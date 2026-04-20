package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.exception.EntityNotFoundException;
import com.portfolio.virtualwallet.repository.UserRepository;
import com.portfolio.virtualwallet.service.interfaces.UserService;
import com.portfolio.virtualwallet.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.portfolio.virtualwallet.utils.AppConstants.EntityFields.ID;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;

@Controller
@RequestMapping(MvcConstants.Paths.ADMIN_USERS)
@RequiredArgsConstructor
public class WebAdminUserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public String showUsers(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            Model model) {

        String currentUsername = SecurityUtils.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException(MvcConstants.Messages.USER_NOT_FOUND));

        model.addAttribute(MvcConstants.Attributes.CURRENT_USER, currentUser);

        Pageable pageable = PageRequest.of(page, size, Sort.by(ID).ascending());

        model.addAttribute(MvcConstants.Attributes.USERS_PAGE, userService.adminSearchUsers(query, pageable));
        model.addAttribute(MvcConstants.Attributes.QUERY, query);

        return MvcConstants.Views.ADMIN_USERS_LIST;
    }

    @PostMapping("/{userId}/block-status")
    public String toggleBlockStatus(
            @PathVariable Long userId,
            @RequestParam boolean isBlocked,
            RedirectAttributes redirectAttributes) {

        userService.toggleUserBlockStatus(userId, isBlocked);

        String message = isBlocked ? MvcConstants.Messages.USER_BLOCKED_SUCCESS : MvcConstants.Messages.USER_UNBLOCKED_SUCCESS;
        redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, message);

        return MvcConstants.Views.REDIRECT_ADMIN_USERS;
    }
}