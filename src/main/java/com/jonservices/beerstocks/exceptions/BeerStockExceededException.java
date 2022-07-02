package com.jonservices.beerstocks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockExceededException extends RuntimeException {

    public BeerStockExceededException(int max) {
        super(String.format("Quantity is less than 0 or stock exceeds max quantity of: %s", max));
    }

}
