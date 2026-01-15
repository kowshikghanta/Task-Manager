package com.kowshik.taskmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
}