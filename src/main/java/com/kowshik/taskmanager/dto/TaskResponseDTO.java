package com.kowshik.taskmanager.dto;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}