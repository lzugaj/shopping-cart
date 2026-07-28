package com.luv2code.shoppingcart.rest.handler;

public record ValidationErrorResponse(String object, String field, Object rejectedValue, String message) {

}
