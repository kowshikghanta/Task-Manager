package com.kowshik.taskmanager.service;

import com.kowshik.taskmanager.dto.UserRequestDTO;
import com.kowshik.taskmanager.dto.UserResponseDTO;
import com.kowshik.taskmanager.entity.User;
import com.kowshik.taskmanager.exception.EmailAlreadyExistsException;
import com.kowshik.taskmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    //Constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {

        //Business Logic: check if email is unique
        if (userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistsException("Email exists");

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}