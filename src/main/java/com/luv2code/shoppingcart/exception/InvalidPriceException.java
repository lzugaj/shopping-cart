package com.luv2code.shoppingcart.exception;

import org.springframework.http.HttpStatus;

public class InvalidPriceException extends ShoppingCartException {

    public InvalidPriceException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                message,
                "price.invalid"
        );
    }
}