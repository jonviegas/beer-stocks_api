package com.jonservices.beerstocks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BeerNotFoundException extends RuntimeException {

    public BeerNotFoundException(String attribute, Object value) {
        super("Beer not found with " + attribute + " " + value);
    }

}
