package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.user.ChangeEmailDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import com.portfolio.virtualwallet.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.EMAIL_CHANGE_SUCCESS;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<Page<UserPublicResponseDto>> searchUsers(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.searchPublicUsers(query, pageable));
    }

    @PatchMapping("/email")
    public ResponseEntity<Map<String, String>> changeEmail(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody ChangeEmailDto request) {

        userService.changeEmail(currentUser, request.getNewEmail());

        return ResponseEntity.ok(Map.of("message", EMAIL_CHANGE_SUCCESS));
    }
}
