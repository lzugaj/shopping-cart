package com.luv2code.shoppingcart.rest.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CartResponse(
        String id,
        String customerId,
        List<CartItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}