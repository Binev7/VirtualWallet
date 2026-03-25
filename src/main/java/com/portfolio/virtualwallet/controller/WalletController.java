package com.portfolio.virtualwallet.controller;

import com.portfolio.virtualwallet.entity.dto.wallet.*;
import com.portfolio.virtualwallet.service.interfaces.JointWalletService;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final JointWalletService jointWalletService;

    @GetMapping
    public ResponseEntity<List<WalletResponseDto>> getMyWallets() {
        return ResponseEntity.ok(walletService.getMyWallets());
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<WalletMemberDto>> getWalletMembers(@PathVariable Long id) {
        return ResponseEntity.ok(jointWalletService.getWalletMembers(id));
    }

    @PostMapping
    public ResponseEntity<WalletResponseDto> createWallet(@Valid @RequestBody WalletCreateDto request) {
        WalletResponseDto createdWallet = walletService.createWallet(request);
        return new ResponseEntity<>(createdWallet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletResponseDto> updateWallet(
            @PathVariable Long id,
            @Valid @RequestBody WalletUpdateDto request) {
        WalletResponseDto updatedWallet = walletService.updateWallet(id, request);
        return ResponseEntity.ok(updatedWallet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addMemberToJointWallet(
            @PathVariable("id") Long walletId,
            @Valid @RequestBody AddWalletMemberDto request) {
        jointWalletService.addMemberToJointWallet(walletId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> updateMemberRights(
            @PathVariable("id") Long walletId,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody UpdateWalletMemberRightsDto request) {
        jointWalletService.updateMemberRights(walletId, userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromJointWallet(
            @PathVariable("id") Long walletId,
            @PathVariable("userId") Long userId) {
        jointWalletService.removeMemberFromJointWallet(walletId, userId);
        return ResponseEntity.noContent().build();
    }
}