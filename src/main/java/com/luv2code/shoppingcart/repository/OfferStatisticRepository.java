package com.luv2code.shoppingcart.repository;

import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.model.OfferStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface OfferStatisticRepository extends MongoRepository<OfferStatistic, String> {

    long countByOfferIdAndActionAndCreatedAtBetween(
            String offerId,
            Action action,
            LocalDateTime from,
            LocalDateTime to
    );
}