package com.luv2code.shoppingcart.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ShoppingCartException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;
    private final String messageKey;
    private final Throwable cause;

    public ShoppingCartException(HttpStatus httpStatus, String message, String messageKey) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.messageKey = messageKey;
        this.cause = null;
    }

    public ShoppingCartException(HttpStatus httpStatus, String message, String messageKey, Throwable cause) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.messageKey = messageKey;
        this.cause = cause;
    }
}