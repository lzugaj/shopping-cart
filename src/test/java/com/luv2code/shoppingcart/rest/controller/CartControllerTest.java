package com.luv2code.shoppingcart.rest.controller;

import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.model.Price;
import com.luv2code.shoppingcart.model.PriceType;
import com.luv2code.shoppingcart.model.RecurrenceUnit;
import com.luv2code.shoppingcart.rest.dto.AddCartItemRequest;
import com.luv2code.shoppingcart.rest.dto.CartItemResponse;
import com.luv2code.shoppingcart.rest.dto.CartResponse;
import com.luv2code.shoppingcart.rest.dto.PriceResponse;
import com.luv2code.shoppingcart.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CartService cartService;

    @Test
    void getCart_existingCustomer_returnsOk() throws Exception {
        CartResponse response = createCartResponse();

        when(cartService.getCart("customer-001"))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/carts/customer-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("customer-001"))
                .andExpect(jsonPath("$.items[0].offerId").value("offer-001"));

        verify(cartService).getCart("customer-001");
        verifyNoMoreInteractions(cartService);
    }

    @Test
    void addItem_validRequest_returnsCreated() throws Exception {
        CartResponse response = createCartResponse();

        when(cartService.addItem(
                eq("customer-001"),
                any(AddCartItemRequest.class)
        )).thenReturn(response);

        mockMvc.perform(post("/api/v1/carts/customer-001/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("customer-001"))
                .andExpect(jsonPath("$.items[0].offerId").value("offer-001"));

        ArgumentCaptor<AddCartItemRequest> captor =
                ArgumentCaptor.forClass(AddCartItemRequest.class);

        verify(cartService).addItem(
                eq("customer-001"),
                captor.capture()
        );

        AddCartItemRequest capturedRequest = captor.getValue();

        assertThat(capturedRequest.id())
                .isEqualTo("item-001");
        assertThat(capturedRequest.offerId())
                .isEqualTo("offer-001");
        assertThat(capturedRequest.action())
                .isEqualTo(Action.ADD);
        assertThat(capturedRequest.prices())
                .hasSize(1);

        verifyNoMoreInteractions(cartService);
    }

    @Test
    void removeItem_existingCart_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/carts/customer-001/items/item-001"))
                .andExpect(status().isNoContent());

        verify(cartService).removeItem("customer-001", "item-001");
        verifyNoMoreInteractions(cartService);
    }

    @Test
    void evictCart_existingCart_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/carts/customer-001"))
                .andExpect(status().isNoContent());

        verify(cartService).evictCart("customer-001");
        verifyNoMoreInteractions(cartService);
    }

    private AddCartItemRequest createRequest() {
        return new AddCartItemRequest(
                "item-001",
                "offer-001",
                Action.ADD,
                List.of(
                        Price.builder()
                                .type(PriceType.RECURRING)
                                .amount(BigDecimal.TEN)
                                .currency("EUR")
                                .recurrences(12)
                                .recurrenceUnit(RecurrenceUnit.MONTH)
                                .build()
                )
        );
    }

    private CartResponse createCartResponse() {
        return CartResponse.builder()
                .id("cart-001")
                .customerId("customer-001")
                .items(List.of(
                        CartItemResponse.builder()
                                .id("item-001")
                                .offerId("offer-001")
                                .action(Action.ADD)
                                .prices(List.of(
                                        PriceResponse.builder()
                                                .type(PriceType.RECURRING)
                                                .amount(BigDecimal.TEN)
                                                .currency("EUR")
                                                .recurrences(12)
                                                .recurrenceUnit(RecurrenceUnit.MONTH)
                                                .build()
                                ))
                                .build()
                ))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}