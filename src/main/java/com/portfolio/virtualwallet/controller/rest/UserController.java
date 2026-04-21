package com.portfolio.virtualwallet.controller.rest;

import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.user.ChangeEmailDto;
import com.portfolio.virtualwallet.entity.dto.user.UserPublicResponseDto;
import com.portfolio.virtualwallet.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.portfolio.virtualwallet.controller.docs.SwaggerMessages.User.*;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_NUMBER;
import static com.portfolio.virtualwallet.utils.AppConstants.Pagination.DEFAULT_PAGE_SIZE;
import static com.portfolio.virtualwallet.utils.AppConstants.SuccessMessages.EMAIL_CHANGE_SUCCESS;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
public class UserController {

    private final UserService userService;

    @Operation(summary = SEARCH_SUMMARY, description = SEARCH_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SUCCESS_200)
    @GetMapping("/search")
    public ResponseEntity<Page<UserPublicResponseDto>> searchUsers(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.searchPublicUsers(query, pageable));
    }

    @Operation(summary = CHANGE_EMAIL_SUMMARY, description = CHANGE_EMAIL_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "409", description = CONFLICT_409, content = @Content)
    })
    @PatchMapping("/email")
    public ResponseEntity<Map<String, String>> changeEmail(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody ChangeEmailDto request) {

        userService.changeEmail(currentUser, request.getNewEmail());

        return ResponseEntity.ok(Map.of("message", EMAIL_CHANGE_SUCCESS));
    }
}