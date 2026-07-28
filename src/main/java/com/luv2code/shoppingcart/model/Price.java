package com.luv2code.shoppingcart.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Price {

    @NotNull
    private PriceType type;

    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @Positive
    private Integer recurrences;

    private RecurrenceUnit recurrenceUnit;

}