package com.luv2code.shoppingcart.rest.controller;

import com.luv2code.shoppingcart.rest.dto.AddCartItemRequest;
import com.luv2code.shoppingcart.rest.dto.CartResponse;
import com.luv2code.shoppingcart.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{customerId}")
    public CartResponse getCart(@PathVariable @NotBlank String customerId) {
        return cartService.getCart(customerId);
    }

    @PostMapping("/{customerId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addItem(
            @PathVariable @NotBlank String customerId,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        return cartService.addItem(customerId, request);
    }

    @DeleteMapping("/{customerId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(
            @PathVariable @NotBlank String customerId,
            @PathVariable @NotBlank String itemId
    ) {
        cartService.removeItem(customerId, itemId);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void evictCart(@PathVariable @NotBlank String customerId) {
        cartService.evictCart(customerId);
    }
}