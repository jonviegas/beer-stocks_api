package com.jonservices.beerstocks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerAlreadyRegisteredException extends RuntimeException {

    public BeerAlreadyRegisteredException(String name) {
        super("Beer with name " + name + " already registered in the system");
    }

}
