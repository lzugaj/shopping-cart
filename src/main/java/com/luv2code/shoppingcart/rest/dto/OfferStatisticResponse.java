package com.luv2code.shoppingcart.rest.dto;

import com.luv2code.shoppingcart.model.Action;

import java.time.LocalDateTime;

public record OfferStatisticResponse(
        String offerId,
        Action action,
        Period period,
        long total
) {

    public record Period(
            LocalDateTime from,
            LocalDateTime to
    ) {
    }
}