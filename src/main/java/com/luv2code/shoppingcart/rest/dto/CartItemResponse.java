package com.luv2code.shoppingcart.rest.dto;

import com.luv2code.shoppingcart.model.Action;
import lombok.Builder;

import java.util.List;

@Builder
public record CartItemResponse(
        String id,
        String offerId,
        Action action,
        List<PriceResponse> prices
) {}