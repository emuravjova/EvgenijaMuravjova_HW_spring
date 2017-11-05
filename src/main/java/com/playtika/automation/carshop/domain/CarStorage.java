package com.playtika.automation.carshop.domain;

import lombok.Data;

@Data
public class CarStorage {
    private final int id = ++gen;
    private static int gen = 0;
    private Car car;
    private CarSaleDetails carSaleDetails;
}
