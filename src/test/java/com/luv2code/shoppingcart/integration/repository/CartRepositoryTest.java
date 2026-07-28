package com.luv2code.shoppingcart.integration.repository;

import com.luv2code.shoppingcart.integration.IntegrationTest;
import com.luv2code.shoppingcart.model.Cart;
import com.luv2code.shoppingcart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void setup() {
        cartRepository.deleteAll();
    }

    @Test
    void findByCustomerId_existingCustomerId_returnsCart() {
        Cart cart = Cart.builder()
                .customerId("customer-001")
                .build();

        cartRepository.save(cart);

        var result = cartRepository.findByCustomerId("customer-001");

        assertThat(result)
                .isPresent()
                .get()
                .extracting(Cart::getCustomerId)
                .isEqualTo("customer-001");
    }

    @Test
    void findByCustomerId_nonExistingCustomerId_returnsEmpty() {
        var result = cartRepository.findByCustomerId("customer-999");

        assertThat(result)
                .isEmpty();
    }
}