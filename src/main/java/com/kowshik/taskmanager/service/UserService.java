package com.kowshik.taskmanager.service;

import com.kowshik.taskmanager.dto.UserResponseDTO;
import com.kowshik.taskmanager.entity.User;
import com.kowshik.taskmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.kowshik.taskmanager.dto.UserResponseDTO;
@Service
public class UserService {
    private final UserRepository userRepository;

    //Constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO createUser(User user) {

        //Business Logic: check if email is unique
        if (userRepository.existsByEmail(user.getEmail()))
            throw new RuntimeException("Email Already Exists");

        //Business Logic: if role doesnt exist, assign default role
        if (user.getRole() == null)
            user.setRole("USER");

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }
}