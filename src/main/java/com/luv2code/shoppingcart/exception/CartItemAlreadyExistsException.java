package com.luv2code.shoppingcart.exception;

import org.springframework.http.HttpStatus;

public class CartItemAlreadyExistsException extends ShoppingCartException {

    public CartItemAlreadyExistsException(String offerId) {
        super(
                HttpStatus.CONFLICT,
                "Offer '%s' already exists in cart.".formatted(offerId),
                "cart.item.already_exists"
        );
    }
}