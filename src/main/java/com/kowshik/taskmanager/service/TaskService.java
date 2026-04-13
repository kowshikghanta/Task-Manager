package com.kowshik.taskmanager.service;

import com.kowshik.taskmanager.entity.User;
import com.kowshik.taskmanager.entity.Task;
import com.kowshik.taskmanager.dto.TaskRequestDTO;
import com.kowshik.taskmanager.dto.TaskResponseDTO;
import com.kowshik.taskmanager.dto.PaginatedResponseDTO;
import com.kowshik.taskmanager.repository.TaskRepository;
import com.kowshik.taskmanager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        return mapToDTO(saved);
    }

    public PaginatedResponseDTO<TaskResponseDTO> getTasksByUser(Long userId, String status, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> tasksPage;

        if (status != null && !status.isEmpty()) {
            tasksPage = taskRepository.findByUserIdAndStatus(userId, status.toUpperCase(), pageable);
        } else {
            tasksPage = taskRepository.findByUserId(userId, pageable);
        }

        List<TaskResponseDTO> content = tasksPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                content,
                tasksPage.getNumber(),
                tasksPage.getSize(),
                tasksPage.getTotalElements(),
                tasksPage.getTotalPages(),
                tasksPage.isLast()
        );
    }

    public TaskResponseDTO updateTaskStatus(Long userId, Long taskId, String status) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new RuntimeException("Task not found or doesn't belong to user"));

        task.setStatus(status.toUpperCase());
        task.setUpdatedAt(LocalDateTime.now());
        Task updatedTask = taskRepository.save(task);
        return mapToDTO(updatedTask);
    }

    public void deleteTask(Long userId, Long taskId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new RuntimeException("Task not found or doesn't belong to user"));
        taskRepository.delete(task);
    }

    private TaskResponseDTO mapToDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }
}