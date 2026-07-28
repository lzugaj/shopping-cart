package com.luv2code.shoppingcart.exception;

import org.springframework.http.HttpStatus;

public class CartItemNotFoundException extends ShoppingCartException {

    public CartItemNotFoundException(String itemId) {
        super(
                HttpStatus.NOT_FOUND,
                "Cart item '%s' not found.".formatted(itemId),
                "cart.item.not_found"
        );
    }
}
