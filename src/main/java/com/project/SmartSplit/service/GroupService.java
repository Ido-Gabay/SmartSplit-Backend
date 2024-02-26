package com.project.SmartSplit.service;

import com.project.SmartSplit.dto.CardDTO;
import com.project.SmartSplit.dto.GroupDTO;
import com.project.SmartSplit.entity.Group;
import com.project.SmartSplit.entity.User;
import com.project.SmartSplit.repository.CardRepository;
import com.project.SmartSplit.repository.GroupRepository;
import com.project.SmartSplit.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    @Autowired
    private UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final CardService cardService;
    private final UserService userService;
    private final CardRepository cardRepository;

    public GroupDTO mapToDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setPurpose(group.getPurpose());
        dto.setOpenDate(group.getOpenDate());
        dto.setMoneyAmount(group.getMoneyAmount());
        List<Long> userIds = groupRepository.findById(group.getId())
                .map(g -> g.getUsers().stream().map(User::getId).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        dto.setUserIds(userIds);

        return dto;
    }

    public Group mapToEntity(GroupDTO dto) {
        Group group = new Group();
        group.setId(dto.getId());
        group.setName(dto.getName());
        group.setPurpose(dto.getPurpose());
        group.setOpenDate(dto.getOpenDate());
        group.setMoneyAmount(dto.getMoneyAmount());
        if (dto.getUserIds() != null && !dto.getUserIds().isEmpty()) {
            // Fetch users from the database based on the provided user IDs
            List<User> users = userRepository.findAllById(dto.getUserIds());
            // Initialize the users set if not already initialized
            if (group.getUsers() == null) {
                group.setUsers(new HashSet<>(users));
            } else {
                // Add the fetched users to the existing set
                group.getUsers().addAll(users);
            }
        }

        return group;
    }

    public List<GroupDTO> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public GroupDTO getGroupById(Long id) {
        Optional<Group> groupOptional = groupRepository.findById(id);
        return groupOptional.map(this::mapToDTO).orElse(null);
    }

    @Transactional
    public GroupDTO createGroup(GroupDTO groupDTO, Long userId) {
        try {
            Group group = mapToEntity(groupDTO);
            Date currentDate = java.sql.Timestamp.valueOf(LocalDateTime.now());
            group.setOpenDate(currentDate);
            // Fetch users from the database based on the provided user IDs
            List<User> users = userRepository.findAllById(groupDTO.getUserIds());
            // Initialize the users set if not already initialized
            if (group.getUsers() == null) {
                group.setUsers(new HashSet<>(users));
            } else {
                // Add the fetched users to the existing set
                group.getUsers().addAll(users);
            }
            // Retrieve the user by userId
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Get the default card for the user from UserService
                CardDTO defaultCard = userService.getDefaultCardByUserId(userId);
                // Set the default card for the user
                if (defaultCard != null) {
                    // Calculate the share for each user
                    int userShare = group.getMoneyAmount() / group.getUsers().size();
                    // Check if each user's default card has enough balance for their share
                    for (User groupUser : group.getUsers()) {
                        CardDTO userDefaultCard = userService.getDefaultCardByUserId(groupUser.getId());
                        int cardBalance = cardService.getBalanceByCardId(userDefaultCard.getId());
                        if (cardBalance < userShare) {
                            // Handle the case where the card balance is insufficient for any user
                            throw new RuntimeException("Insufficient balance in the default card for the user with ID: " + groupUser.getId());
                        }
                    }
                    // Deduct the share from each user's default card
                    for (User groupUser : group.getUsers()) {
                        CardDTO userDefaultCard = userService.getDefaultCardByUserId(groupUser.getId());
                        int cardBalance = cardService.getBalanceByCardId(userDefaultCard.getId());
                        // Deduct the share from the user's default card
                        cardBalance -= userShare;
                        // Update the user's default card balance
                        cardService.updateCardBalance(userDefaultCard.getId(), cardBalance);
                    }
                    // Save the group
                    Group savedGroup = groupRepository.save(group);
                    // Add the group to the user's groups
                    user.getGroups().add(savedGroup);
                    userRepository.save(user);
                    return mapToDTO(savedGroup);
                } else {
                    // Handle the case where no default card is found for the user
                    throw new RuntimeException("Default card not found for the user with ID: " + userId);
                }
            } else {
                throw new RuntimeException("User not found with ID: " + userId);
            }
        } catch (Exception e) {
            // Handle any exceptions and log the details
            e.printStackTrace();
            // You may want to throw a more specific exception or handle it according to your needs
            throw new RuntimeException("An error occurred while creating the group.");
        }
    }


    public GroupDTO updateGroup(Long id, GroupDTO groupDTO) {
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (optionalGroup.isPresent()) {
            Group groupToUpdate = optionalGroup.get();
            groupToUpdate.setName(groupDTO.getName());
            groupToUpdate.setPurpose(groupDTO.getPurpose());
            groupToUpdate.setOpenDate(groupDTO.getOpenDate());
            groupToUpdate.setMoneyAmount(groupDTO.getMoneyAmount());
            Group updatedGroup = groupRepository.save(groupToUpdate);
            return mapToDTO(updatedGroup);
        }
        return null;
    }

    public boolean deleteGroup(Long id) {
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (optionalGroup.isPresent()) {
            Group groupToDelete = optionalGroup.get();

            // Remove the group from the users' groups
            for (User user : groupToDelete.getUsers()) {
                user.getGroups().remove(groupToDelete);
                userRepository.save(user);
            }

            // Clear the users from the group to avoid cascading issues
            groupToDelete.getUsers().clear();
            groupRepository.save(groupToDelete);

            groupRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<GroupDTO> getGroupsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<Group> userGroups = user.getGroups();

            return userGroups.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }



}

