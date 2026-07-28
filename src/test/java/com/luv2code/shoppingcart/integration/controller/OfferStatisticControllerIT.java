package com.luv2code.shoppingcart.integration.controller;

import com.luv2code.shoppingcart.integration.IntegrationTest;
import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.model.OfferStatistic;
import com.luv2code.shoppingcart.repository.OfferStatisticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

@IntegrationTest
class OfferStatisticControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private OfferStatisticRepository offerStatisticRepository;

    @BeforeEach
    void cleanDatabase() {
        offerStatisticRepository.deleteAll();
    }

    @Test
    void getOfferStatistics_existingStatistics_returnsCount() {
        String offerId = "offer-001";

        offerStatisticRepository.save(
                OfferStatistic.builder()
                        .offerId(offerId)
                        .action(Action.ADD)
                        .createdAt(LocalDateTime.of(2026, 7, 10, 10, 0))
                        .build()
        );

        offerStatisticRepository.save(
                OfferStatistic.builder()
                        .offerId(offerId)
                        .action(Action.ADD)
                        .createdAt(LocalDateTime.of(2026, 7, 20, 10, 0))
                        .build()
        );

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/statistics/offers/{offerId}")
                        .queryParam("action", "ADD")
                        .queryParam("from", "2026-07-01T00:00:00")
                        .queryParam("to", "2026-07-28T23:59:59")
                        .build(offerId))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.offerId")
                .isEqualTo("offer-001")
                .jsonPath("$.action")
                .isEqualTo("ADD")
                .jsonPath("$.total")
                .isEqualTo(2);
    }

    @Test
    void getOfferStatistics_noStatistics_returnsZero() {

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/statistics/offers/{offerId}")
                        .queryParam("action", "DELETE")
                        .queryParam("from", "2026-07-01T00:00:00")
                        .queryParam("to", "2026-07-28T23:59:59")
                        .build("unknown-offer"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.total")
                .isEqualTo(0);
    }
}