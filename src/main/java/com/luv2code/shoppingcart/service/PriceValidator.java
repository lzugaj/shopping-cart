package com.luv2code.shoppingcart.service;

import com.luv2code.shoppingcart.exception.InvalidPriceException;
import com.luv2code.shoppingcart.model.CartItem;
import com.luv2code.shoppingcart.model.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceValidator {

    public void validate(CartItem item) {
        item.getPrices().forEach(this::validatePrice);
    }

    private void validatePrice(Price price) {
        switch (price.getType()) {
            case ONE_TIME -> validateOneTime(price);
            case RECURRING -> validateRecurring(price);
        }
    }

    private void validateOneTime(Price price) {
        if (price.getRecurrences() != null ||
                price.getRecurrenceUnit() != null) {
            throw new InvalidPriceException("One-time price cannot contain recurrence information.");
        }
    }

    private void validateRecurring(Price price) {
        if (price.getRecurrences() == null ||
                price.getRecurrences() <= 0 ||
                price.getRecurrenceUnit() == null) {
            throw new InvalidPriceException("Recurring price requires recurrence information.");
        }
    }
}
