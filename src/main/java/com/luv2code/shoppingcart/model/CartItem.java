package com.luv2code.shoppingcart.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItem {

    @NotBlank
    private String id;

    @NotBlank
    private String offerId;

    @NotNull
    private Action action;

    @NotEmpty
    private List<@Valid Price> prices;

}