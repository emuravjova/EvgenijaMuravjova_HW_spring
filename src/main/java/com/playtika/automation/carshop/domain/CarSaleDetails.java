package com.playtika.automation.carshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarSaleDetails {
    private long id;
    private Car car;
    private int price;
    private String contacts;
}
