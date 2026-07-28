package com.luv2code.shoppingcart.rest.handler;

import com.luv2code.shoppingcart.exception.ShoppingCartException;
import com.luv2code.shoppingcart.util.ClockUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ShoppingCartExceptionHandler {

    @ExceptionHandler(ShoppingCartException.class)
    public ResponseEntity<ApiErrorResponse> handleShoppingCartException(
            ShoppingCartException exception,
            HttpServletRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(ClockUtil.getClock()),
                exception.getHttpStatus(),
                exception.getMessage(),
                exception.getMessageKey(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<ValidationErrorResponse> errors =
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> new ValidationErrorResponse(
                                error.getObjectName(),
                                error.getField(),
                                error.getRejectedValue(),
                                error.getDefaultMessage()
                        ))
                        .toList();

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(ClockUtil.getClock()),
                BAD_REQUEST,
                "Validation failed for one or more fields.",
                "validation.error.message",
                request.getRequestURI(),
                errors
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {

        log.error("Database constraint violation occurred", exception);

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(ClockUtil.getClock()),
                CONFLICT,
                "A data conflict occurred. The request could not be processed.",
                "data.integrity.violation.message",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(CONFLICT)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleDefaultException(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error("Unexpected error occurred", exception);

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(ClockUtil.getClock()),
                INTERNAL_SERVER_ERROR,
                "Sorry, we encountered an unexpected issue while processing your request. Please try again later.",
                "default.error.message",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(response);
    }
}