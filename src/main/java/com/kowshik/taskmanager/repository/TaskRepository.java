package com.kowshik.taskmanager.repository;

import com.kowshik.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUserId(Long userId, Pageable pageable);
    Page<Task> findByUserIdAndStatus(Long userId, String status, Pageable pageable);
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    long countByStatus(String status);
}