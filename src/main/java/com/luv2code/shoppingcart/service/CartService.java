package com.luv2code.shoppingcart.service;

import com.luv2code.shoppingcart.exception.CartNotFoundException;
import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.model.Cart;
import com.luv2code.shoppingcart.model.CartItem;
import com.luv2code.shoppingcart.model.OfferStatistic;
import com.luv2code.shoppingcart.repository.CartRepository;
import com.luv2code.shoppingcart.repository.OfferStatisticRepository;
import com.luv2code.shoppingcart.rest.dto.AddCartItemRequest;
import com.luv2code.shoppingcart.rest.dto.CartResponse;
import com.luv2code.shoppingcart.rest.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final PriceValidator priceValidator;
    private final OfferStatisticRepository offerStatisticRepository;

    public CartResponse getCart(String customerId) {
        return cartMapper.toResponse(getExistingCart(customerId));
    }

    public CartResponse addItem(String customerId, AddCartItemRequest request) {
        CartItem item = cartMapper.toEntity(request);

        priceValidator.validate(item);

        Cart cart = getOrCreateCart(customerId);
        cart.addItem(item);

        Cart savedCart = cartRepository.save(cart);

        saveOfferStatistic(item.getOfferId(), item.getAction());

        return cartMapper.toResponse(savedCart);
    }

    public void removeItem(String customerId, String itemId) {
        Cart cart = getExistingCart(customerId);

        CartItem item = cart.getItem(itemId);

        cart.removeItem(itemId);

        cartRepository.save(cart);

        saveOfferStatistic(item.getOfferId(), Action.DELETE);
    }

    public void evictCart(String customerId) {
        Cart cart = getExistingCart(customerId);

        cart.getItems()
                .forEach(item -> saveOfferStatistic(item.getOfferId(), Action.DELETE));

        cartRepository.delete(cart);
    }

    private Cart getExistingCart(String customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));
    }

    private Cart getOrCreateCart(String customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> createCart(customerId));
    }

    private Cart createCart(String customerId) {
        Cart cart = Cart.builder()
                .customerId(customerId)
                .build();

        return cartRepository.save(cart);
    }

    private void saveOfferStatistic(String offerId, Action action) {
        offerStatisticRepository.save(
                OfferStatistic.builder()
                        .offerId(offerId)
                        .action(action)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}
