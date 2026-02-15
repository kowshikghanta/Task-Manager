package com.kowshik.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class TaskRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;
}