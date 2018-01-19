package com.playtika.automation.carshop.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by emuravjova on 1/19/2018.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class NoBestDealForOpenOfferException extends RuntimeException {
    public NoBestDealForOpenOfferException(String message) {
        super(message);
    }
}
