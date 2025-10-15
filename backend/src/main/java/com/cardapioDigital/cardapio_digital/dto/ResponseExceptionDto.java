package com.cardapioDigital.cardapio_digital.dto;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseExceptionDto(
                                   LocalDateTime timestamp,
                                   Integer status,
                                   String error,
                                   String message,
                                   String path
) {
    public ResponseExceptionDto(@NotNull HttpStatus status, String message, String path) {
        this(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }
}