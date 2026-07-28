package com.luv2code.shoppingcart.rest.controller;

import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.rest.dto.OfferStatisticResponse;
import com.luv2code.shoppingcart.service.OfferStatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OfferStatisticController.class)
class OfferStatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OfferStatisticService offerStatisticService;

    @Test
    void getOfferStatistics_validRequest_returnsStatistics() throws Exception {
        String offerId = "offer-001";

        LocalDateTime from = LocalDateTime.of(2026, 7, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2026, 7, 28, 23, 59);

        OfferStatisticResponse response =
                new OfferStatisticResponse(
                        offerId,
                        Action.ADD,
                        new OfferStatisticResponse.Period(from, to),
                        100
                );

        when(offerStatisticService.getStatistics(
                eq(offerId),
                eq(Action.ADD),
                eq(from),
                eq(to)
        )).thenReturn(response);

        mockMvc.perform(get("/api/v1/statistics/offers/{offerId}", offerId)
                        .param("action", "ADD")
                        .param("from", "2026-07-01T00:00:00")
                        .param("to", "2026-07-28T23:59:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offerId").value("offer-001"))
                .andExpect(jsonPath("$.action").value("ADD"))
                .andExpect(jsonPath("$.period.from")
                        .value("2026-07-01T00:00:00"))
                .andExpect(jsonPath("$.period.to")
                        .value("2026-07-28T23:59:00"))
                .andExpect(jsonPath("$.total").value(100));

        verify(offerStatisticService)
                .getStatistics(
                        offerId,
                        Action.ADD,
                        from,
                        to
                );
    }
}