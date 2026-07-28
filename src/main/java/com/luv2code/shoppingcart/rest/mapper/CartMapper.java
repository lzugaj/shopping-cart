package com.luv2code.shoppingcart.rest.mapper;

import com.luv2code.shoppingcart.model.Cart;
import com.luv2code.shoppingcart.model.CartItem;
import com.luv2code.shoppingcart.model.Price;
import com.luv2code.shoppingcart.rest.dto.AddCartItemRequest;
import com.luv2code.shoppingcart.rest.dto.CartItemResponse;
import com.luv2code.shoppingcart.rest.dto.CartResponse;
import com.luv2code.shoppingcart.rest.dto.PriceResponse;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public CartItem toEntity(AddCartItemRequest request) {
        return CartItem.builder()
                .id(request.id())
                .offerId(request.offerId())
                .action(request.action())
                .prices(request.prices())
                .build();
    }


    public CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .customerId(cart.getCustomerId())
                .items(
                        cart.getItems()
                                .stream()
                                .map(this::toResponse)
                                .toList()
                )
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }


    private CartItemResponse toResponse(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .offerId(item.getOfferId())
                .action(item.getAction())
                .prices(
                        item.getPrices()
                                .stream()
                                .map(this::toResponse)
                                .toList()
                )
                .build();
    }


    private PriceResponse toResponse(Price price) {
        return PriceResponse.builder()
                .type(price.getType())
                .amount(price.getAmount())
                .currency(price.getCurrency())
                .recurrences(price.getRecurrences())
                .recurrenceUnit(price.getRecurrenceUnit())
                .build();
    }
}