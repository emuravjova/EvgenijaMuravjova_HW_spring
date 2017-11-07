package com.playtika.automation.carshop.domain;

import lombok.Data;

@Data
public class CarStorage {
    private long id;
    private Car car;
    private double price;
    private String contacts;
}
