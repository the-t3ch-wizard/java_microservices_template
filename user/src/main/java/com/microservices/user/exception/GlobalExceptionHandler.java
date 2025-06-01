package com.microservices.user.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.microservices.user.utils.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(CustomException ex, WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
            .success(false)
            .message(ex.getMessage())
            .status(ex.getStatus().value())
            .data(new ErrorResponse.ErrorDetails(LocalDateTime.now(), request.getDescription(false)))
            .build();

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .message(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .data(new ErrorResponse.ErrorDetails(LocalDateTime.now(), request.getDescription(false)))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
}
