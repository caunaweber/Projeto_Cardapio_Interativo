package com.cardapioDigital.cardapio_digital.Exception;

import com.cardapioDigital.cardapio_digital.dto.ResponseExceptionDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

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

}
