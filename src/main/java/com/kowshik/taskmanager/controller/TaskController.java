package com.kowshik.taskmanager.controller;


import com.kowshik.taskmanager.dto.TaskRequestDTO;
import com.kowshik.taskmanager.dto.TaskResponseDTO;
import com.kowshik.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/tasks")
public class TaskController {
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @PathVariable Long userId,
            @Valid @RequestBody TaskRequestDTO request) {

        return ResponseEntity.ok(taskService.createTask(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }
}