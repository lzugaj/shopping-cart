package com.luv2code.shoppingcart.exception;

import org.springframework.http.HttpStatus;

public class CartNotFoundException extends ShoppingCartException {

    public CartNotFoundException(String customerId) {
        super(
                HttpStatus.NOT_FOUND,
                "Cart for customer '%s' not found.".formatted(customerId),
                "cart.not_found"
        );
    }
}