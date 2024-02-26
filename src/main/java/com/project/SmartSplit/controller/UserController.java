package com.project.SmartSplit.controller;

import com.project.SmartSplit.dto.CardDTO;
import com.project.SmartSplit.service.CardService;
import com.project.SmartSplit.service.UserService;
import  com.project.SmartSplit.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final CardService cardService;

    @Autowired
    public UserController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        return userService.updateUser(id,userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/email/{Email}")
    public ResponseEntity<Long> getUserIdByEmail(@PathVariable("Email") String email) {
        Long userId = userService.getUserIdByEmail(email);
        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/setDefaultCard/{userId}/{cardId}")
    public ResponseEntity<Void> setDefaultCard(@PathVariable Long userId, @PathVariable Long cardId) {
        userService.setDefaultCard(userId, cardId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/defaultCard/{userId}")
    public ResponseEntity<CardDTO> getDefaultCardByUserId(@PathVariable Long userId) {
        CardDTO defaultCard = userService.getDefaultCardByUserId(userId);

        if (defaultCard != null) {
            return ResponseEntity.ok(defaultCard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
