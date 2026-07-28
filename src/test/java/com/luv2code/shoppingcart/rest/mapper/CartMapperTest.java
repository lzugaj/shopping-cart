package com.luv2code.shoppingcart.rest.mapper;

import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.model.Cart;
import com.luv2code.shoppingcart.model.CartItem;
import com.luv2code.shoppingcart.model.Price;
import com.luv2code.shoppingcart.model.PriceType;
import com.luv2code.shoppingcart.model.RecurrenceUnit;
import com.luv2code.shoppingcart.rest.dto.AddCartItemRequest;
import com.luv2code.shoppingcart.rest.dto.CartItemResponse;
import com.luv2code.shoppingcart.rest.dto.CartResponse;
import com.luv2code.shoppingcart.rest.dto.PriceResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartMapperTest {

    private final CartMapper cartMapper = new CartMapper();

    @Test
    void toEntity_validRequest_returnsCartItem() {
        Price price = Price.builder()
                .type(PriceType.RECURRING)
                .amount(BigDecimal.valueOf(20))
                .currency("EUR")
                .recurrences(12)
                .recurrenceUnit(RecurrenceUnit.MONTH)
                .build();

        AddCartItemRequest request = new AddCartItemRequest(
                "item-001",
                "offer-001",
                Action.ADD,
                List.of(price)
        );

        CartItem result = cartMapper.toEntity(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(request.id());
        assertThat(result.getOfferId()).isEqualTo(request.offerId());
        assertThat(result.getAction()).isEqualTo(request.action());
        assertThat(result.getPrices()).containsExactly(price);
    }

    @Test
    void toResponse_validCart_returnsCartResponse() {
        Price price = Price.builder()
                .type(PriceType.RECURRING)
                .amount(BigDecimal.valueOf(20))
                .currency("EUR")
                .recurrences(12)
                .recurrenceUnit(RecurrenceUnit.MONTH)
                .build();

        CartItem item = CartItem.builder()
                .id("item-001")
                .offerId("offer-001")
                .action(Action.ADD)
                .prices(List.of(price))
                .build();

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusMinutes(5);

        Cart cart = Cart.builder()
                .id("cart-001")
                .customerId("customer-001")
                .items(List.of(item))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        CartResponse response = cartMapper.toResponse(cart);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(cart.getId());
        assertThat(response.customerId()).isEqualTo(cart.getCustomerId());
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);

        assertThat(response.items()).hasSize(1);

        CartItemResponse itemResponse = response.items().getFirst();

        assertThat(itemResponse.id()).isEqualTo(item.getId());
        assertThat(itemResponse.offerId()).isEqualTo(item.getOfferId());
        assertThat(itemResponse.action()).isEqualTo(item.getAction());

        assertThat(itemResponse.prices()).hasSize(1);

        PriceResponse priceResponse = itemResponse.prices().getFirst();

        assertThat(priceResponse.type()).isEqualTo(price.getType());
        assertThat(priceResponse.amount()).isEqualTo(price.getAmount());
        assertThat(priceResponse.currency()).isEqualTo(price.getCurrency());
        assertThat(priceResponse.recurrences()).isEqualTo(price.getRecurrences());
        assertThat(priceResponse.recurrenceUnit()).isEqualTo(price.getRecurrenceUnit());
    }
}