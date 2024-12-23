package com.vijay.User_Master.exceptions;

import com.vijay.User_Master.Helper.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Logger instance for the class
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error("Bad credentials for user: {}", ex.getMessage(), ex);
        return ExceptionUtil.createErrorResponseMessage("Incorrect username or password", HttpStatus.UNAUTHORIZED);
    }

    // Handler for UserAlreadyExistsException (specific to user creation)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        // Log the exception details
        logger.error("User already exists: {}", ex.getMessage(), ex);
        // Create and return the error response using ExceptionUtil
        return ExceptionUtil.createErrorResponseMessage(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // Handle Resource Not Found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        // Log the exception details
        logger.error("Resource not found: {}", ex.getMessage(), ex);
        // Return the error response
        return ExceptionUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle Validation Exceptions (e.g., @Valid errors)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        // Log the validation errors
        logger.error("Validation error: {}", ex.getMessage(), ex);

        // Extract field errors into a map for cleaner response
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage()));

        // Return the validation errors response
        return ExceptionUtil.createErrorResponse(validationErrors, HttpStatus.BAD_REQUEST);
    }

    // Handle Generic Exceptions (catch-all for unhandled exceptions)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        // Log the unexpected error
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        // Return the generic error response
        return ExceptionUtil.createErrorResponseMessage("An unexpected error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(Exception e) {
        logger.error("GlobalExceptionHandler :: handleNullPointerException ::", e.getMessage());
        // return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ExceptionUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //handle bad api exception
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<?> handleBadApiRequest(BadApiRequestException ex) {
        logger.info("Bad api request");
        return ExceptionUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.BAD_REQUEST);

    }
}


