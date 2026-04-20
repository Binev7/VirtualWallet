package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.User;
import com.portfolio.virtualwallet.entity.dto.wallet.*;
import com.portfolio.virtualwallet.exception.UnauthorizedException;
import com.portfolio.virtualwallet.exception.WalletNotEmptyException;
import com.portfolio.virtualwallet.service.interfaces.JointWalletService;
import com.portfolio.virtualwallet.service.interfaces.WalletService;
import com.portfolio.virtualwallet.utils.TransactionValidationHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants.Messages.UNEXPECTED_ERROR;

@Controller
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WebWalletController {

    private final WalletService walletService;
    private final JointWalletService jointWalletService;
    private final TransactionValidationHelper transactionValidationHelper;

    @GetMapping
    public String listMyWallets(Model model) {
        model.addAttribute(MvcConstants.Attributes.WALLETS, walletService.getMyWallets());
        return MvcConstants.Views.WALLETS_LIST;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute(MvcConstants.Attributes.WALLET_DTO, new WalletCreateDto());
        return MvcConstants.Views.WALLET_FORM;
    }

    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        var wallet = transactionValidationHelper.getWalletById(id);

        WalletUpdateDto updateDto = WalletUpdateDto.builder()
                .name(wallet.getName())
                .build();

        model.addAttribute(MvcConstants.Attributes.WALLET, id);
        model.addAttribute(MvcConstants.Attributes.WALLET_DTO, updateDto);

        return MvcConstants.Views.WALLET_FORM;
    }

    @PostMapping("/new")
    public String createWallet(@Valid @ModelAttribute(MvcConstants.Attributes.WALLET_CREATE_REQUEST) WalletCreateDto dto,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return MvcConstants.Views.WALLET_FORM;

        walletService.createWallet(dto);
        redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.WALLET_CREATED_SUCCESS);
        return MvcConstants.Views.REDIRECT_WALLETS;
    }

    @PostMapping("/{id}/edit")
    public String updateWallet(@PathVariable Long id,
                               @Valid @ModelAttribute(MvcConstants.Attributes.WALLET_UPDATE_REQUEST) WalletUpdateDto dto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return MvcConstants.Views.WALLET_FORM;
        }

        walletService.updateWallet(id, dto);
        redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE,
                MvcConstants.Messages.WALLET_UPDATED_SUCCESS);

        return MvcConstants.Views.REDIRECT_WALLETS;
    }

    @PostMapping("/{id}/delete")
    public String deleteWallet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            walletService.deleteWallet(id);
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE,
                    MvcConstants.Messages.WALLET_DELETED_SUCCESS);
        } catch (WalletNotEmptyException | UnauthorizedException e) {
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, UNEXPECTED_ERROR);
        }

        return MvcConstants.Views.REDIRECT_WALLETS;
    }

    @GetMapping("/{id}/members")
    public String manageMembers(@PathVariable Long id,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                @ModelAttribute(MvcConstants.Attributes.CURRENT_USER) User currentUser) {

        if (currentUser == null) return MvcConstants.Views.REDIRECT_LOGIN;

        try {
            var members = jointWalletService.getWalletMembers(id);

            boolean isOwner = members.stream()
                    .anyMatch(m -> m.getUsername().equals(currentUser.getUsername()) && m.isOwner());

            model.addAttribute(MvcConstants.Attributes.WALLET, id);
            model.addAttribute(MvcConstants.Attributes.MEMBERS, members);
            model.addAttribute(MvcConstants.Attributes.ADD_MEMBER_REQUEST, new AddWalletMemberDto());
            model.addAttribute(MvcConstants.Attributes.CURRENT_USER_NAME, currentUser.getUsername());
            model.addAttribute(MvcConstants.Attributes.IS_OWNER, isOwner);

            return MvcConstants.Views.WALLET_MEMBERS;
        } catch (UnauthorizedException e) {
            redirectAttributes.addFlashAttribute(MvcConstants.Attributes.ERROR, MvcConstants.Messages.MEMBERS_MANAGE);
            return MvcConstants.Views.REDIRECT_WALLETS;
        }
    }

    @PostMapping("/{id}/members/add")
    public String addMember(@PathVariable Long id,
                            @Valid @ModelAttribute(MvcConstants.Attributes.ADD_MEMBER_REQUEST) AddWalletMemberDto dto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return MvcConstants.getWalletMembersRedirect(id);
        }

        jointWalletService.addMemberToJointWallet(id, dto);
        redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.MEMBER_ADDED_SUCCESS);

        return MvcConstants.getWalletMembersRedirect(id);
    }

    @PostMapping("/{id}/members/{userId}/update")
    public String updateMemberRights(@PathVariable Long id,
                                     @PathVariable Long userId,
                                     @ModelAttribute UpdateWalletMemberRightsDto dto,
                                     RedirectAttributes redirectAttributes) {

        jointWalletService.updateMemberRights(id, userId, dto);

        redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE,
                MvcConstants.Messages.SUCCESSFULLY_UPDATED_MEMBER_RIGHTS);

        return MvcConstants.getWalletMembersRedirect(id);
    }

    @PostMapping("/{id}/members/{userId}/delete")
    public String removeMember(@PathVariable Long id, @PathVariable Long userId, RedirectAttributes redirectAttributes) {
        jointWalletService.removeMemberFromJointWallet(id, userId);
        redirectAttributes.addFlashAttribute(MvcConstants.Attributes.SUCCESS_MESSAGE, MvcConstants.Messages.MEMBER_REMOVED_SUCCESS);

        return MvcConstants.getWalletMembersRedirect(id);
    }
}