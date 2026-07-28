package com.luv2code.shoppingcart.service;

import com.luv2code.shoppingcart.model.Action;
import com.luv2code.shoppingcart.repository.OfferStatisticRepository;
import com.luv2code.shoppingcart.rest.dto.OfferStatisticResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferStatisticServiceTest {

    @Mock
    private OfferStatisticRepository offerStatisticRepository;

    @InjectMocks
    private OfferStatisticService offerStatisticService;

    @Test
    void getStatistics_existingStatistics_returnsStatisticsResponse() {
        String offerId = "offer-001";
        Action action = Action.ADD;

        LocalDateTime from = LocalDateTime.of(2026, 7, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2026, 7, 28, 23, 59);

        when(offerStatisticRepository.countByOfferIdAndActionAndCreatedAtBetween(
                offerId,
                action,
                from,
                to
        )).thenReturn(125L);

        OfferStatisticResponse result = offerStatisticService.getStatistics(
                offerId,
                action,
                from,
                to
        );

        assertThat(result.offerId())
                .isEqualTo(offerId);

        assertThat(result.action())
                .isEqualTo(Action.ADD);

        assertThat(result.period().from())
                .isEqualTo(from);

        assertThat(result.period().to())
                .isEqualTo(to);

        assertThat(result.total())
                .isEqualTo(125);

        verify(offerStatisticRepository)
                .countByOfferIdAndActionAndCreatedAtBetween(
                        offerId,
                        action,
                        from,
                        to
                );

        verifyNoMoreInteractions(offerStatisticRepository);
    }

    @Test
    void getStatistics_noStatistics_returnsZeroCount() {
        String offerId = "offer-001";
        Action action = Action.DELETE;

        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();

        when(offerStatisticRepository.countByOfferIdAndActionAndCreatedAtBetween(
                offerId,
                action,
                from,
                to
        )).thenReturn(0L);

        OfferStatisticResponse result = offerStatisticService.getStatistics(
                offerId,
                action,
                from,
                to
        );

        assertThat(result.total())
                .isZero();

        assertThat(result.offerId())
                .isEqualTo(offerId);

        assertThat(result.action())
                .isEqualTo(Action.DELETE);

        verify(offerStatisticRepository)
                .countByOfferIdAndActionAndCreatedAtBetween(
                        offerId,
                        action,
                        from,
                        to
                );

        verifyNoMoreInteractions(offerStatisticRepository);
    }
}