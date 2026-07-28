package com.luv2code.shoppingcart.service;

import com.luv2code.shoppingcart.exception.InvalidPriceException;
import com.luv2code.shoppingcart.model.CartItem;
import com.luv2code.shoppingcart.model.Price;
import com.luv2code.shoppingcart.model.PriceType;
import com.luv2code.shoppingcart.model.RecurrenceUnit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceValidatorTest {

    private final PriceValidator priceValidator = new PriceValidator();

    @Test
    void validate_validOneTimePrice_doesNotThrowException() {
        CartItem item = CartItem.builder()
                .prices(List.of(
                        Price.builder()
                                .type(PriceType.ONE_TIME)
                                .amount(BigDecimal.TEN)
                                .currency("EUR")
                                .build()
                ))
                .build();

        assertThatCode(() -> priceValidator.validate(item))
                .doesNotThrowAnyException();
    }

    @Test
    void validate_oneTimePriceWithRecurrence_throwsInvalidPriceException() {
        CartItem item = CartItem.builder()
                .prices(List.of(
                        Price.builder()
                                .type(PriceType.ONE_TIME)
                                .amount(BigDecimal.TEN)
                                .currency("EUR")
                                .recurrences(12)
                                .recurrenceUnit(RecurrenceUnit.MONTH)
                                .build()
                ))
                .build();

        assertThatThrownBy(() -> priceValidator.validate(item))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("One-time price cannot contain recurrence information.");
    }

    @Test
    void validate_validRecurringPrice_doesNotThrowException() {
        CartItem item = CartItem.builder()
                .prices(List.of(
                        Price.builder()
                                .type(PriceType.RECURRING)
                                .amount(BigDecimal.TEN)
                                .currency("EUR")
                                .recurrences(24)
                                .recurrenceUnit(RecurrenceUnit.MONTH)
                                .build()
                ))
                .build();

        assertThatCode(() -> priceValidator.validate(item))
                .doesNotThrowAnyException();
    }

    @Test
    void validate_recurringPriceWithoutRecurrenceInformation_throwsInvalidPriceException() {
        CartItem item = CartItem.builder()
                .prices(List.of(
                        Price.builder()
                                .type(PriceType.RECURRING)
                                .amount(BigDecimal.TEN)
                                .currency("EUR")
                                .build()
                ))
                .build();

        assertThatThrownBy(() -> priceValidator.validate(item))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("Recurring price requires recurrence information.");
    }
}