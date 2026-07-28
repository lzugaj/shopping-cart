package com.luv2code.shoppingcart.rest.controller;

import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.rest.dto.OfferStatisticResponse;
import com.luv2code.shoppingcart.service.OfferStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class OfferStatisticController {

    private final OfferStatisticService offerStatisticService;

    @GetMapping("/offers/{offerId}")
    public OfferStatisticResponse getOfferStatistics(
            @PathVariable String offerId,
            @RequestParam Action action,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return offerStatisticService.getStatistics(
                offerId,
                action,
                from,
                to
        );
    }
}