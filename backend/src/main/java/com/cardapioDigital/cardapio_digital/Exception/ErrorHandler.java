package com.cardapioDigital.cardapio_digital.Exception;

import com.cardapioDigital.cardapio_digital.dto.ResponseExceptionDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseValidationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseExceptionDto> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        ResponseExceptionDto errorDto = new ResponseExceptionDto(
                (HttpStatus) ex.getStatusCode(),
                ex.getReason(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ResponseValidationDto>> handleValidationException(MethodArgumentNotValidException ex) {

        List<ResponseValidationDto> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ResponseValidationDto(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
