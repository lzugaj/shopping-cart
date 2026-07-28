package com.luv2code.shoppingcart.model;

import com.luv2code.shoppingcart.exception.CartItemAlreadyExistsException;
import com.luv2code.shoppingcart.exception.CartItemNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {

    @Test
    void addItem_newItem_addsItemToCart() {
        Cart cart = Cart.builder()
                .customerId("customer-001")
                .build();

        CartItem item = createCartItem("item-001", "offer-001");

        cart.addItem(item);

        assertThat(cart.getItems())
                .containsExactly(item);
    }

    @Test
    void addItem_existingOfferId_throwsCartItemAlreadyExistsException() {
        Cart cart = Cart.builder()
                .customerId("customer-001")
                .items(List.of(
                        createCartItem("item-001", "offer-001")
                ))
                .build();

        CartItem item = createCartItem("item-002", "offer-001");

        assertThatThrownBy(() -> cart.addItem(item))
                .isInstanceOf(CartItemAlreadyExistsException.class)
                .hasMessageContaining("offer-001");
    }

    @Test
    void removeItem_existingItemId_removesItemFromCart() {
        CartItem item = createCartItem("item-001", "offer-001");

        Cart cart = Cart.builder()
                .customerId("customer-001")
                .items(new ArrayList<>(List.of(item)))
                .build();

        cart.removeItem("item-001");

        assertThat(cart.getItems())
                .isEmpty();
    }

    @Test
    void removeItem_nonExistingItemId_throwsCartItemNotFoundException() {
        Cart cart = Cart.builder()
                .customerId("customer-001")
                .items(new ArrayList<>(List.of(
                        createCartItem("item-001", "offer-001")
                )))
                .build();

        assertThatThrownBy(() -> cart.removeItem("item-002"))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessageContaining("item-002");
    }

    @Test
    void getItem_existingItemId_returnsItem() {
        CartItem item = CartItem.builder()
                .id("item-1")
                .offerId("offer-123")
                .build();

        Cart cart = Cart.builder()
                .items(List.of(item))
                .build();

        CartItem result = cart.getItem("item-1");

        assertThat(result)
                .isNotNull()
                .isEqualTo(item);
    }

    @Test
    void getItem_nonExistingItemId_throwsCartItemNotFoundException() {
        CartItem item = CartItem.builder()
                .id("item-1")
                .offerId("offer-123")
                .build();

        Cart cart = Cart.builder()
                .items(List.of(item))
                .build();

        assertThatThrownBy(() -> cart.getItem("item-999"))
                .isInstanceOf(CartItemNotFoundException.class)
                .hasMessageContaining("item-999");
    }

    private CartItem createCartItem(String id, String offerId) {
        return CartItem.builder()
                .id(id)
                .offerId(offerId)
                .action(Action.ADD)
                .prices(List.of())
                .build();
    }
}