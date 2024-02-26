package com.project.SmartSplit.service;

import com.project.SmartSplit.dto.CardDTO;
import com.project.SmartSplit.entity.Card;
import com.project.SmartSplit.entity.Group;
import com.project.SmartSplit.entity.User;
import com.project.SmartSplit.entity.Role;
import com.project.SmartSplit.dto.UserDTO;
import com.project.SmartSplit.repository.GroupRepository;
import com.project.SmartSplit.repository.RoleRepository;
import com.project.SmartSplit.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final CardService cardService;

    public UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        return dto;
    }

    public User mapToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setAge(dto.getAge());
        return user;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(this::mapToDTO).orElse(null);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = mapToEntity(userDTO);
        Role defaultRole = roleRepository.findByName("USER");
        user.setRoles(List.of(defaultRole));
        User savedUser = userRepository.save(user);

        return mapToDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();
            userToUpdate.setFirstName(userDTO.getFirstName());
            userToUpdate.setLastName(userDTO.getLastName());
            userToUpdate.setEmail(userDTO.getEmail());
            userToUpdate.setPassword(userDTO.getPassword());
            userToUpdate.setAge(userDTO.getAge());
            User updatedUser = userRepository.save(userToUpdate);
            return mapToDTO(updatedUser);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? user.getId() : null;
    }
    @Transactional
    public void setDefaultCard(Long userId, Long cardId) {
        User user = userRepository.findById(userId).orElse(null);
        Card card = cardService.getCardById(cardId);

        if (user != null && card != null) {
            user.setDefaultCard(card);
            userRepository.save(user);
        }
    }
    public CardDTO getDefaultCardByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null && user.getDefaultCard() != null) {
            // Map the default card entity to CardDTO
            return cardService.mapToDTO(user.getDefaultCard());
        }

        return null;
    }
}
