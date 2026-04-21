package com.portfolio.virtualwallet.controller.rest;

import com.portfolio.virtualwallet.entity.dto.card.CardCreateDto;
import com.portfolio.virtualwallet.entity.dto.card.CardResponseDto;
import com.portfolio.virtualwallet.entity.dto.card.CardUpdateDto;
import com.portfolio.virtualwallet.service.interfaces.CardService;
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

import static com.portfolio.virtualwallet.controller.docs.SwaggerMessages.Card.*;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
public class CardController {

    private final CardService cardService;

    @Operation(summary = ADD_CARD_SUMMARY, description = ADD_CARD_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = CREATED_201),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content)
    })
    @PostMapping
    public ResponseEntity<CardResponseDto> addCard(@Valid @RequestBody CardCreateDto request) {
        return new ResponseEntity<>(cardService.addCard(request), HttpStatus.CREATED);
    }

    @Operation(summary = GET_ALL_SUMMARY, description = GET_ALL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SUCCESS_200)
    @GetMapping
    public ResponseEntity<List<CardResponseDto>> getAllMyCards() {
        return ResponseEntity.ok(cardService.getAllMyCards());
    }

    @Operation(summary = GET_BY_ID_SUMMARY, description = GET_BY_ID_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDto> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @Operation(summary = UPDATE_CARD_SUMMARY, description = UPDATE_CARD_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = SUCCESS_200),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST_400, content = @Content),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long id, @Valid @RequestBody CardUpdateDto request) {
        return ResponseEntity.ok(cardService.updateCard(id, request));
    }

    @Operation(summary = DELETE_CARD_SUMMARY, description = DELETE_CARD_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = SUCCESS_204),
            @ApiResponse(responseCode = "403", description = FORBIDDEN_403, content = @Content),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404, content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}