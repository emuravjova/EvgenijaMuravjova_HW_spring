package com.playtika.automation.carshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

/**
 * Created by emuravjova on 11/20/2017.
 */
@Value
@AllArgsConstructor
public class SaleInfo {
    private int price;
    private String contacts;
}
