package com.vijay.User_Master.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handler for UserAlreadyExistsException (specific to user creation)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.BAD_REQUEST,
                "error",
                ex.getMessage(),
                "Username or email already exists",
                LocalDateTime.now()
        );
        return errorDetails.create();
    }

    // Handler for ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.NOT_FOUND,
                "error",
                ex.getMessage(),
                String.format("Resource: %s, Field: %s, Value: %s",
                        ex.getResourceName(),
                        ex.getFieldName(),
                        ex.getFieldValue()),
                LocalDateTime.now()
        );
        return errorDetails.create();
    }

    // Handler for NoSuchElementException (e.g., when role or other elements are not found)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.NOT_FOUND,
                "error",
                "Resource not found",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return errorDetails.create();
    }

    // General RuntimeException handler for unexpected exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "error",
                "Internal server error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return errorDetails.create();
    }

    // General Exception handler for all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "error",
                "An unexpected error occurred",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return errorDetails.create();
    }
}

