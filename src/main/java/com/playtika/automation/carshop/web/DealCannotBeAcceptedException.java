package com.playtika.automation.carshop.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by emuravjova on 1/19/2018.
 */
@ResponseStatus(value= HttpStatus.CONFLICT)
public class DealCannotBeAcceptedException  extends RuntimeException {
    public DealCannotBeAcceptedException(String message) {
        super(message);
    }
}
