package com.luv2code.shoppingcart.rest.dto;

import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.model.Price;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record AddCartItemRequest(
        @NotBlank String id,
        @NotBlank String offerId,
        @NotNull Action action,
        @NotEmpty List<@Valid Price> prices
) {}