package com.luv2code.shoppingcart.rest.dto;

import com.luv2code.shoppingcart.model.PriceType;
import com.luv2code.shoppingcart.model.RecurrenceUnit;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PriceResponse(
        PriceType type,
        BigDecimal amount,
        String currency,
        Integer recurrences,
        RecurrenceUnit recurrenceUnit
) {}