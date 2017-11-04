package com.playtika.automation.carshop.domain;

import lombok.Data;

@Data
public class Car {
    private int id;
    private String model;
    private double price;
    private String owner;
}
