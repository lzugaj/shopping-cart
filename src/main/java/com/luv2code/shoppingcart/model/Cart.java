package com.luv2code.shoppingcart.model;

import com.luv2code.shoppingcart.exception.CartItemAlreadyExistsException;
import com.luv2code.shoppingcart.exception.CartItemNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String customerId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<@Valid CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        boolean exists = items.stream()
                .anyMatch(existing ->
                        existing.getOfferId().equals(item.getOfferId()));

        if (exists) {
            throw new CartItemAlreadyExistsException(item.getOfferId());
        }

        items.add(item);
    }

    public void removeItem(String itemId) {
        if (!items.removeIf(item -> item.getId().equals(itemId))) {
            throw new CartItemNotFoundException(itemId);
        }
    }

    public CartItem getItem(String itemId) {
        return items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(itemId));
    }
}