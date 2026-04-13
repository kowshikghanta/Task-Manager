package com.kowshik.taskmanager.service;

import com.kowshik.taskmanager.dto.PaginatedResponseDTO;
import com.kowshik.taskmanager.dto.TaskResponseDTO;
import com.kowshik.taskmanager.entity.Task;
import com.kowshik.taskmanager.entity.User;
import com.kowshik.taskmanager.repository.TaskRepository;
import com.kowshik.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTasksByUser_WithStatusFilter() {
        // Arrange
        Long userId = 1L;
        String status = "PENDING";
        
        User user = new User();
        user.setId(userId);
        
        Task task = new Task(1L, "Test Title", "Desc", "PENDING", LocalDateTime.now(), null, user);
        Page<Task> expectedPage = new PageImpl<>(List.of(task), PageRequest.of(0, 10), 1);
        
        when(taskRepository.findByUserIdAndStatus(eq(userId), eq("PENDING"), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        PaginatedResponseDTO<TaskResponseDTO> result = taskService.getTasksByUser(userId, status, 0, 10);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("PENDING", result.getContent().get(0).getStatus());
        assertEquals(0, result.getPageNo());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
    }

    @Test
    void testGetTasksByUser_WithoutStatusFilter() {
        // Arrange
        Long userId = 1L;
        
        User user = new User();
        user.setId(userId);
        
        Task task1 = new Task(1L, "Task 1", "Desc", "PENDING", LocalDateTime.now(), null, user);
        Task task2 = new Task(2L, "Task 2", "Desc", "COMPLETED", LocalDateTime.now(), null, user);
        
        Page<Task> expectedPage = new PageImpl<>(List.of(task1, task2), PageRequest.of(0, 5), 2);
        
        when(taskRepository.findByUserId(eq(userId), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        PaginatedResponseDTO<TaskResponseDTO> result = taskService.getTasksByUser(userId, null, 0, 5);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPageNo());
        assertEquals(5, result.getPageSize());
        assertEquals(2, result.getTotalElements());
    }
}
