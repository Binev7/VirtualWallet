package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.dto.user.UserDetailsAdminDto;
import com.portfolio.virtualwallet.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.portfolio.virtualwallet.controller.docs.SwaggerMessages.AdminUser.*;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = GET_ALL_SUMMARY, description = GET_ALL_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<UserDetailsAdminDto>> getAllUsersForAdmin(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(userService.adminSearchUsers(query, pageable));
    }

    @Operation(summary = TOGGLE_BLOCK_SUMMARY, description = TOGGLE_BLOCK_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = SUCCESS_204),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PatchMapping("/{userId}/block-status")
    public ResponseEntity<Void> toggleBlockStatus(
            @PathVariable Long userId,
            @RequestParam boolean isBlocked) {

        userService.toggleUserBlockStatus(userId, isBlocked);
        return ResponseEntity.noContent().build();
    }
}