package com.luv2code.shoppingcart.service;

import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.repository.OfferStatisticRepository;
import com.luv2code.shoppingcart.rest.dto.OfferStatisticResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OfferStatisticService {

    private final OfferStatisticRepository offerStatisticRepository;

    public OfferStatisticResponse getStatistics(
            String offerId,
            Action action,
            LocalDateTime from,
            LocalDateTime to
    ) {
        long count = offerStatisticRepository
                .countByOfferIdAndActionAndCreatedAtBetween(
                        offerId,
                        action,
                        from,
                        to
                );

        return new OfferStatisticResponse(
                offerId,
                action,
                new OfferStatisticResponse.Period(from, to),
                count
        );
    }
}