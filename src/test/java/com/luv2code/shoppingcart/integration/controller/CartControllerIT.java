package com.luv2code.shoppingcart.integration.controller;

import com.luv2code.shoppingcart.integration.IntegrationTest;
import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.model.Price;
import com.luv2code.shoppingcart.model.PriceType;
import com.luv2code.shoppingcart.model.RecurrenceUnit;
import com.luv2code.shoppingcart.repository.CartRepository;
import com.luv2code.shoppingcart.rest.dto.AddCartItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class CartControllerIT {

    private static final String CUSTOMER_ID = "customer-001";
    private static final String ITEM_ID = "item-001";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void setup() {
        cartRepository.deleteAll();
    }

    @Test
    void getCart_existingCustomerId_returnsCart() {
        AddCartItemRequest request = createRequest();

        webTestClient.post()
                .uri("/api/v1/carts/{customerId}/items", CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isCreated();

        webTestClient.get()
                .uri("/api/v1/carts/{customerId}", CUSTOMER_ID)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.customerId")
                .isEqualTo(CUSTOMER_ID)
                .jsonPath("$.items.length()")
                .isEqualTo(1)
                .jsonPath("$.items[0].id")
                .isEqualTo(ITEM_ID);
    }

    @Test
    void getCart_nonExistingCustomerId_returnsNotFound() {
        webTestClient.get()
                .uri("/api/v1/carts/{customerId}", CUSTOMER_ID)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void addItem_validRequest_returnsCreatedAndStoresItem() {
        webTestClient.post()
                .uri("/api/v1/carts/{customerId}/items", CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest())
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.customerId")
                .isEqualTo(CUSTOMER_ID)
                .jsonPath("$.items[0].offerId")
                .isEqualTo("offer-001");

        assertThat(cartRepository.findByCustomerId(CUSTOMER_ID))
                .isPresent();
    }

    @Test
    void removeItem_existingItemId_returnsNoContent() {
        webTestClient.post()
                .uri("/api/v1/carts/{customerId}/items", CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest())
                .exchange()
                .expectStatus()
                .isCreated();

        webTestClient.delete()
                .uri("/api/v1/carts/{customerId}/items/{itemId}", CUSTOMER_ID, ITEM_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        assertThat(cartRepository.findByCustomerId(CUSTOMER_ID))
                .get()
                .extracting(cart -> cart.getItems())
                .asList()
                .isEmpty();
    }

    @Test
    void removeItem_nonExistingItemId_returnsError() {
        webTestClient.delete()
                .uri("/api/v1/carts/{customerId}/items/{itemId}", CUSTOMER_ID, ITEM_ID)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void evictCart_existingCustomerId_returnsNoContent() {
        webTestClient.post()
                .uri("/api/v1/carts/{customerId}/items", CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createRequest())
                .exchange()
                .expectStatus()
                .isCreated();

        webTestClient.delete()
                .uri("/api/v1/carts/{customerId}", CUSTOMER_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        assertThat(cartRepository.findByCustomerId(CUSTOMER_ID))
                .isEmpty();
    }

    private AddCartItemRequest createRequest() {
        Price price = Price.builder()
                .type(PriceType.RECURRING)
                .amount(BigDecimal.TEN)
                .currency("EUR")
                .recurrences(12)
                .recurrenceUnit(RecurrenceUnit.MONTH)
                .build();

        return new AddCartItemRequest(
                ITEM_ID,
                "offer-001",
                Action.ADD,
                List.of(price)
        );
    }
}