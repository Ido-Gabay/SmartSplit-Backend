package com.project.SmartSplit.controller;

import com.project.SmartSplit.dto.CardDTO;
import com.project.SmartSplit.entity.Card;
import com.project.SmartSplit.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public List<CardDTO> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable Long id) {
        Card card = cardService.getCardById(id);

        if (card != null) {
            CardDTO cardDTO = cardService.mapToDTO(card);
            return ResponseEntity.ok(cardDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}")
    public CardDTO createCard(@RequestBody CardDTO cardDTO, @PathVariable Long userId) {
        return cardService.createCard(cardDTO, userId);
    }

    @PutMapping("{id}")
    public CardDTO updateCard(@PathVariable Long id, @RequestBody CardDTO cardDTO) {
        cardDTO.setId(id);
        return cardService.updateCard(id, cardDTO);
    }

    @DeleteMapping("{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardDTO>> getCardsByUserId(@PathVariable Long userId) {
        List<CardDTO> userCards = cardService.getCardsByUserId(userId);

        if (userCards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or handle as appropriate
        }

        return new ResponseEntity<>(userCards, HttpStatus.OK);
    }
}
