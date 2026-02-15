package com.kowshik.taskmanager.service;

import com.kowshik.taskmanager.entity.User;
import com.kowshik.taskmanager.entity.Task;
import com.kowshik.taskmanager.dto.TaskRequestDTO;
import com.kowshik.taskmanager.dto.TaskResponseDTO;
import com.kowshik.taskmanager.repository.TaskRepository;
import com.kowshik.taskmanager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponseDTO createTask(Long userID, TaskRequestDTO request) {

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not Found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCreatedAt(LocalDateTime.now());
        task.setStatus("PENDING");
        task.setUser(user);

        Task saved = taskRepository.save(task);

        return new TaskResponseDTO(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }

    public List<TaskResponseDTO> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId)
                .stream()
                .map(task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}