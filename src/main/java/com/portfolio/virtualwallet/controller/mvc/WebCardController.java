package com.portfolio.virtualwallet.controller.mvc;

import com.portfolio.virtualwallet.controller.mvc.constants.MvcConstants;
import com.portfolio.virtualwallet.entity.dto.card.CardCreateDto;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import com.portfolio.virtualwallet.entity.dto.card.CardUpdateDto;
import com.portfolio.virtualwallet.service.interfaces.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cards")
@RequiredArgsConstructor
public class WebCardController {

    private final CardService cardService;

    @Value("${stripe.api.public-key}")
    private String stripePublicKey;

    @GetMapping
    public String showMyCards(Model model) {
        model.addAttribute(MvcConstants.Attributes.CARDS, cardService.getAllMyCards());
        return MvcConstants.Views.MY_CARDS;
    }

    @GetMapping("/add")
    public String showAddCardPage(Model model) {
        model.addAttribute(MvcConstants.Attributes.CARD, new CardCreateDto());
        model.addAttribute(MvcConstants.Attributes.STRIPE_PUBLIC_KEY, stripePublicKey);

        return MvcConstants.Views.ADD_CARD;
    }

    @PostMapping("/add")
    public String handleAddCard(
            @ModelAttribute(MvcConstants.Attributes.CARD) CardCreateDto request,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            cardService.addCard(request);

            redirectAttributes.addFlashAttribute(
                    MvcConstants.Attributes.SUCCESS_MESSAGE,
                    MvcConstants.Messages.CARD_ADDED_SUCCESS
            );

            return MvcConstants.Views.REDIRECT_MY_CARDS;
        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
            model.addAttribute(MvcConstants.Attributes.STRIPE_PUBLIC_KEY, stripePublicKey);
            return MvcConstants.Views.ADD_CARD;
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditCardPage(@PathVariable Long id, Model model) {
        CardResponseDto card = cardService.getCardById(id);

        model.addAttribute(MvcConstants.Attributes.CARD_ID, id);
        model.addAttribute(MvcConstants.Attributes.CARD, card);
        return MvcConstants.Views.EDIT_CARD;
    }

    @PostMapping("/{id}/update")
    public String handleUpdateCard(
            @PathVariable Long id,
            @ModelAttribute(MvcConstants.Attributes.CARD) CardUpdateDto request,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            cardService.updateCard(id, request);

            redirectAttributes.addFlashAttribute(
                    MvcConstants.Attributes.SUCCESS_MESSAGE,
                    MvcConstants.Messages.CARD_UPDATED_SUCCESS
            );

            return MvcConstants.Views.REDIRECT_MY_CARDS;
        } catch (Exception e) {
            model.addAttribute(MvcConstants.Attributes.ERROR, e.getMessage());
            model.addAttribute(MvcConstants.Attributes.CARD_ID, id);
            return MvcConstants.Views.EDIT_CARD;
        }
    }

    @PostMapping("/{id}/delete")
    public String handleDeleteCard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cardService.deleteCard(id);

            redirectAttributes.addFlashAttribute(
                    MvcConstants.Attributes.SUCCESS_MESSAGE,
                    MvcConstants.Messages.CARD_DELETED_SUCCESS
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    MvcConstants.Attributes.ERROR,
                    e.getMessage()
            );
        }
        return MvcConstants.Views.REDIRECT_MY_CARDS;
    }
}