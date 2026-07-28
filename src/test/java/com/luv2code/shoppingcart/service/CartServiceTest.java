package com.luv2code.shoppingcart.service;

import com.luv2code.shoppingcart.exception.CartNotFoundException;
import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.model.Cart;
import com.luv2code.shoppingcart.model.CartItem;
import com.luv2code.shoppingcart.model.Price;
import com.luv2code.shoppingcart.repository.CartRepository;
import com.luv2code.shoppingcart.repository.OfferStatisticRepository;
import com.luv2code.shoppingcart.rest.dto.AddCartItemRequest;
import com.luv2code.shoppingcart.rest.dto.CartResponse;
import com.luv2code.shoppingcart.rest.mapper.CartMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private PriceValidator priceValidator;

    @Mock
    private OfferStatisticRepository offerStatisticRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void getCart_existingCustomer_returnsCart() {
        String customerId = "customer-001";

        Cart cart = Cart.builder()
                .customerId(customerId)
                .build();

        CartResponse response = mock(CartResponse.class);

        when(cartRepository.findByCustomerId(customerId))
                .thenReturn(Optional.of(cart));
        when(cartMapper.toResponse(cart))
                .thenReturn(response);

        CartResponse result = cartService.getCart(customerId);

        assertThat(result).isSameAs(response);

        verify(cartRepository).findByCustomerId(customerId);
        verify(cartMapper).toResponse(cart);

        verify(cartRepository, never()).save(any());
        verify(priceValidator, never()).validate(any());

        verifyNoMoreInteractions(
                cartRepository,
                cartMapper,
                priceValidator
        );
    }

    @Test
    void getCart_nonExistingCustomer_throwsCartNotFoundException() {
        String customerId = "customer-001";

        when(cartRepository.findByCustomerId(customerId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCart(customerId))
                .isInstanceOf(CartNotFoundException.class);

        verify(cartRepository).findByCustomerId(customerId);

        verify(cartRepository, never()).save(any());
        verify(cartMapper, never()).toResponse(any());
        verify(priceValidator, never()).validate(any());

        verifyNoMoreInteractions(
                cartRepository,
                cartMapper,
                priceValidator
        );
    }

    @Test
    void addItem_existingCart_returnsUpdatedCartAndStoresStatistic() {
        String customerId = "customer-001";

        AddCartItemRequest request = mock(AddCartItemRequest.class);

        CartItem item = CartItem.builder()
                .id("item-001")
                .offerId("offer-001")
                .action(Action.ADD)
                .prices(List.of(mock(Price.class)))
                .build();

        Cart cart = Cart.builder()
                .customerId(customerId)
                .build();

        CartResponse response = mock(CartResponse.class);

        when(cartMapper.toEntity(request))
                .thenReturn(item);

        when(cartRepository.findByCustomerId(customerId))
                .thenReturn(Optional.of(cart));

        when(cartRepository.save(cart))
                .thenReturn(cart);

        when(cartMapper.toResponse(cart))
                .thenReturn(response);

        CartResponse result = cartService.addItem(customerId, request);

        assertThat(result).isSameAs(response);
        assertThat(cart.getItems()).contains(item);

        verify(cartMapper).toEntity(request);
        verify(priceValidator).validate(item);

        verify(cartRepository).findByCustomerId(customerId);
        verify(cartRepository).save(cart);

        verify(offerStatisticRepository).save(
                argThat(statistic ->
                        statistic.getOfferId().equals("offer-001")
                                && statistic.getAction() == Action.ADD
                                && statistic.getCreatedAt() != null
                )
        );

        verify(cartMapper).toResponse(cart);

        verifyNoMoreInteractions(
                cartRepository,
                cartMapper,
                priceValidator,
                offerStatisticRepository
        );
    }

    @Test
    void addItem_nonExistingCart_createsCartAndReturnsUpdatedCart() {
        String customerId = "customer-001";

        AddCartItemRequest request = mock(AddCartItemRequest.class);
        CartItem item = mock(CartItem.class);

        Cart createdCart = Cart.builder()
                .customerId(customerId)
                .build();

        CartResponse response = mock(CartResponse.class);

        when(cartMapper.toEntity(request)).thenReturn(item);
        when(cartRepository.findByCustomerId(customerId))
                .thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(createdCart);
        when(cartMapper.toResponse(createdCart))
                .thenReturn(response);

        CartResponse result = cartService.addItem(customerId, request);

        assertThat(result).isSameAs(response);

        verify(cartMapper).toEntity(request);
        verify(priceValidator).validate(item);
        verify(cartRepository, times(1)).findByCustomerId(customerId);
        verify(cartRepository, times(2)).save(any(Cart.class));
        verify(cartMapper).toResponse(createdCart);

        verifyNoMoreInteractions(
                cartRepository,
                cartMapper,
                priceValidator
        );
    }

    @Test
    void removeItem_existingCart_removesItemAndSavesCart() {
        String customerId = "customer-001";
        String itemId = "item-001";

        CartItem item = CartItem.builder()
                .id(itemId)
                .offerId("offer-001")
                .action(Action.ADD)
                .prices(List.of(mock(Price.class)))
                .build();

        Cart cart = Cart.builder()
                .customerId(customerId)
                .build();

        cart.addItem(item);

        when(cartRepository.findByCustomerId(customerId))
                .thenReturn(Optional.of(cart));

        cartService.removeItem(customerId, itemId);

        assertThat(cart.getItems()).isEmpty();

        verify(cartRepository).findByCustomerId(customerId);
        verify(cartRepository).save(cart);

        verify(offerStatisticRepository).save(
                argThat(statistic ->
                        statistic.getOfferId().equals("offer-001")
                                && statistic.getAction() == Action.DELETE
                )
        );

        verifyNoMoreInteractions(
                cartRepository,
                cartMapper,
                priceValidator,
                offerStatisticRepository
        );
    }

    @Test
    void removeItem_nonExistingCart_throwsCartNotFoundException() {
        String customerId = "customer-001";

        when(cartRepository.findByCustomerId(customerId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                cartService.removeItem(customerId, "item-001"))
                .isInstanceOf(CartNotFoundException.class);

        verify(cartRepository).findByCustomerId(customerId);

        verify(cartRepository, never()).save(any());
        verifyNoMoreInteractions(
                cartRepository,
                cartMapper,
                priceValidator
        );
    }

    @Test
    void evictCart_existingCart_deletesCartAndCreatesStatistics() {
        String customerId = "customer-001";

        CartItem item = CartItem.builder()
                .id("item-001")
                .offerId("offer-001")
                .build();

        Cart cart = Cart.builder()
                .customerId(customerId)
                .build();

        cart.addItem(item);

        when(cartRepository.findByCustomerId(customerId))
                .thenReturn(Optional.of(cart));

        cartService.evictCart(customerId);

        verify(cartRepository).findByCustomerId(customerId);
        verify(cartRepository).delete(cart);

        verify(offerStatisticRepository).save(
                argThat(statistic ->
                        statistic.getOfferId().equals("offer-001")
                                && statistic.getAction() == Action.DELETE
                )
        );

        verify(cartRepository, never()).save(any());

        verifyNoMoreInteractions(
                cartRepository,
                cartMapper,
                priceValidator,
                offerStatisticRepository
        );
    }

    @Test
    void evictCart_nonExistingCart_throwsCartNotFoundException() {
        String customerId = "customer-001";

        when(cartRepository.findByCustomerId(customerId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                cartService.evictCart(customerId))
                .isInstanceOf(CartNotFoundException.class);

        verify(cartRepository).findByCustomerId(customerId);

        verify(cartRepository, never()).delete(any());
        verify(cartRepository, never()).save(any());

        verifyNoMoreInteractions(
                cartRepository,
                cartMapper,
                priceValidator
        );
    }
}