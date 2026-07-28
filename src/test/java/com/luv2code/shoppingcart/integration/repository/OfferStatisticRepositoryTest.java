package com.luv2code.shoppingcart.integration.repository;

import com.luv2code.shoppingcart.integration.IntegrationTest;
import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.model.OfferStatistic;
import com.luv2code.shoppingcart.repository.OfferStatisticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class OfferStatisticRepositoryTest {

    @Autowired
    private OfferStatisticRepository offerStatisticRepository;

    @BeforeEach
    void setup() {
        offerStatisticRepository.deleteAll();
    }

    @Test
    void countByOfferIdAndActionAndCreatedAtBetween_existingStatistics_returnsCount() {
        String offerId = "offer-001";

        offerStatisticRepository.save(
                OfferStatistic.builder()
                        .offerId(offerId)
                        .action(Action.ADD)
                        .createdAt(LocalDateTime.now().minusHours(1))
                        .build()
        );

        offerStatisticRepository.save(
                OfferStatistic.builder()
                        .offerId(offerId)
                        .action(Action.ADD)
                        .createdAt(LocalDateTime.now().minusMinutes(30))
                        .build()
        );

        offerStatisticRepository.save(
                OfferStatistic.builder()
                        .offerId(offerId)
                        .action(Action.DELETE)
                        .createdAt(LocalDateTime.now().minusMinutes(10))
                        .build()
        );

        long result = offerStatisticRepository.countByOfferIdAndActionAndCreatedAtBetween(
                offerId,
                Action.ADD,
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now()
        );

        assertThat(result).isEqualTo(2);
    }

    @Test
    void countByOfferIdAndActionAndCreatedAtBetween_noMatchingStatistics_returnsZero() {
        long result = offerStatisticRepository.countByOfferIdAndActionAndCreatedAtBetween(
                "unknown-offer",
                Action.ADD,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now()
        );

        assertThat(result).isZero();
    }
}