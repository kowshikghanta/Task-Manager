package com.kowshik.taskmanager.controller;

import com.kowshik.taskmanager.dto.PaginatedResponseDTO;
import com.kowshik.taskmanager.dto.TaskRequestDTO;
import com.kowshik.taskmanager.dto.TaskResponseDTO;
import com.kowshik.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

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
    public ResponseEntity<PaginatedResponseDTO<TaskResponseDTO>> getTasks(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId, status, page, size));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(
            @PathVariable Long userId,
            @PathVariable Long taskId,
            @RequestBody Map<String, String> statusUpdate
    ) {
        String status = statusUpdate.get("status");
        if (status == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(taskService.updateTaskStatus(userId, taskId, status));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long userId,
            @PathVariable Long taskId
    ) {
        taskService.deleteTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }
}