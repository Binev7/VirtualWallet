package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.dto.wallet.*;
import com.portfolio.virtualwallet.service.interfaces.JointWalletService;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.portfolio.virtualwallet.controller.docs.SwaggerMessages.Wallet.*;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
public class WalletController {

    private final WalletService walletService;
    private final JointWalletService jointWalletService;

    @Operation(summary = GET_MY_WALLETS_SUMMARY, description = GET_MY_WALLETS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SUCCESS_200)
    @GetMapping
    public ResponseEntity<List<WalletResponseDto>> getMyWallets() {
        return ResponseEntity.ok(walletService.getMyWallets());
    }

    @Operation(summary = GET_MEMBERS_SUMMARY, description = GET_MEMBERS_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @GetMapping("/{id}/members")
    public ResponseEntity<List<WalletMemberDto>> getWalletMembers(@PathVariable Long id) {
        return ResponseEntity.ok(jointWalletService.getWalletMembers(id));
    }

    @Operation(summary = CREATE_WALLET_SUMMARY, description = CREATE_WALLET_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = CREATED_201),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content)
    })
    @PostMapping
    public ResponseEntity<WalletResponseDto> createWallet(@Valid @RequestBody WalletCreateDto request) {
        WalletResponseDto createdWallet = walletService.createWallet(request);
        return new ResponseEntity<>(createdWallet, HttpStatus.CREATED);
    }

    @Operation(summary = UPDATE_WALLET_SUMMARY, description = UPDATE_WALLET_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<WalletResponseDto> updateWallet(
            @PathVariable Long id,
            @Valid @RequestBody WalletUpdateDto request) {
        WalletResponseDto updatedWallet = walletService.updateWallet(id, request);
        return ResponseEntity.ok(updatedWallet);
    }

    @Operation(summary = DELETE_WALLET_SUMMARY, description = DELETE_WALLET_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = SUCCESS_204),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = ADD_MEMBER_SUMMARY, description = ADD_MEMBER_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = CREATED_201),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addMemberToJointWallet(
            @PathVariable("id") Long walletId,
            @Valid @RequestBody AddWalletMemberDto request) {
        jointWalletService.addMemberToJointWallet(walletId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = UPDATE_RIGHTS_SUMMARY, description = UPDATE_RIGHTS_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PutMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> updateMemberRights(
            @PathVariable("id") Long walletId,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody UpdateWalletMemberRightsDto request) {
        jointWalletService.updateMemberRights(walletId, userId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = REMOVE_MEMBER_SUMMARY, description = REMOVE_MEMBER_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = SUCCESS_204),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromJointWallet(
            @PathVariable("id") Long walletId,
            @PathVariable("userId") Long userId) {
        jointWalletService.removeMemberFromJointWallet(walletId, userId);
        return ResponseEntity.noContent().build();
    }
}