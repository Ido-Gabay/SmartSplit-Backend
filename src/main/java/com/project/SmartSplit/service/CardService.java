package com.project.SmartSplit.service;

import com.project.SmartSplit.dto.CardDTO;
import com.project.SmartSplit.entity.Card;
import com.project.SmartSplit.entity.User;
import com.project.SmartSplit.repository.CardRepository;
import com.project.SmartSplit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public CardDTO mapToDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setCardNumbers(card.getCardNumbers());
        dto.setCardName(card.getCardName());
        dto.setCvv(card.getCvv());
        dto.setCardYear(card.getCardYear());
        dto.setCardMonth(card.getCardMonth());
        dto.setBalance(card.getBalance());
        return dto;
    }

    public Card mapToEntity(CardDTO dto) {
        Card card = new Card();
        card.setId(dto.getId());
        card.setCardNumbers(dto.getCardNumbers());
        card.setCardName(dto.getCardName());
        card.setCvv(dto.getCvv());
        card.setCardYear(dto.getCardYear());
        card.setCardMonth(dto.getCardMonth());
        card.setBalance(10000);
        return card;
    }

    public List<CardDTO> getAllCards() {
        List<Card> cards = cardRepository.findAll();
        return cards.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Card getCardById(Long id) {
        Optional<Card> cardOptional = cardRepository.findById(id);
        return cardOptional.orElse(null);
    }

    public CardDTO createCard(CardDTO cardDTO, Long userId) {
        // Map CardDTO to Card entity
        Card card = mapToEntity(cardDTO);
        // Fetch the user from the database using the provided userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

        // Check if the user has no cards (assuming a one-to-many relationship between User and Card)
        if (user.getCards().isEmpty()) {
            // Set the user for the card
            card.setUser(user);
            // Set this card as the default card for the user
            user.setDefaultCard(card);
        } else {
            // Set the user for the card
            card.setUser(user);
        }
        // Save the card
        Card savedCard = cardRepository.save(card);
        // If it's the first card, save the user with the default card
        if (user.getCards().isEmpty()) {
            userRepository.save(user);
        }
        // Map the saved card entity back to CardDTO and return
        return mapToDTO(savedCard);
    }

    public CardDTO updateCard(Long id, CardDTO cardDTO) {
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (optionalCard.isPresent()) {
            Card cardToUpdate = optionalCard.get();
            cardToUpdate.setCardNumbers(cardDTO.getCardNumbers());
            cardToUpdate.setCvv(cardDTO.getCvv());
            cardToUpdate.setCardYear(cardDTO.getCardYear());
            cardToUpdate.setCardMonth(cardDTO.getCardMonth());
            Card updatedCard = cardRepository.save(cardToUpdate);
            return mapToDTO(updatedCard);
        }
        return null;
    }

    public boolean deleteCard(Long id) {
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (optionalCard.isPresent()) {
            cardRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<CardDTO> getCardsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Card> userCards = user.getCards();
            return userCards.stream().map(this::mapToDTO).collect(Collectors.toList());
        }

        return Collections.emptyList(); // or return null if you prefer
    }
    public int getBalanceByCardId(Long cardId) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);

        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();
            return card.getBalance();
        }
        return 0;
    }
    public void updateCardBalance(Long cardId, int newBalance) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);

        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();
            card.setBalance(newBalance);
            cardRepository.save(card);
        } else {
            throw new RuntimeException("Card not found with ID: " + cardId);
        }
    }
}
